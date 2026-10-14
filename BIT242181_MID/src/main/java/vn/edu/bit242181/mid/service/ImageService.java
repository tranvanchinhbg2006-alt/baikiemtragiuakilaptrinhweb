package vn.edu.bit242181.mid.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.exception.NotFoundException;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import vn.edu.bit242181.mid.model.DishImage_BIT242181;
import vn.edu.bit242181.mid.repository.DishImageRepository;
import java.net.URI;

@Service
public class ImageService {
    private final DishImageRepository images;
    private final DishService dishes;
    public ImageService(DishImageRepository images, DishService dishes) {
        this.images = images;
        this.dishes = dishes;
    }
    @Transactional
    public void add(Long dishId, String imageUrl, boolean thumbnail) {
        Dish_BIT242181 dish = dishes.get(dishId);
        validateUrl(imageUrl);
        boolean first = dish.getImages().isEmpty();
        if (thumbnail || first) images.clearThumbnail(dishId);
        // Reload after clearAutomatically to avoid stale persistence context.
        dish = dishes.get(dishId);
        DishImage_BIT242181 image = new DishImage_BIT242181();
        image.setDish(dish);
        image.setImageUrl(imageUrl.trim());
        image.setThumbnail(thumbnail || first);
        images.saveAndFlush(image);
    }
    @Transactional
    public void makeThumbnail(Long dishId, Long imageId) {
        DishImage_BIT242181 image = getOwned(dishId, imageId);
        if (image.isThumbnail()) return;
        images.clearThumbnail(dishId);
        image = getOwned(dishId, imageId);
        image.setThumbnail(true);
        images.saveAndFlush(image);
    }
    @Transactional
    public void delete(Long dishId, Long imageId) {
        DishImage_BIT242181 image = getOwned(dishId, imageId);
        boolean wasThumbnail = image.isThumbnail();
        images.delete(image);
        images.flush();
        if (wasThumbnail) {
            images.findFirstByDishIdOrderByIdAsc(dishId).ifPresent(next -> {
                next.setThumbnail(true);
                images.saveAndFlush(next);
            });
        }
    }
    private DishImage_BIT242181 getOwned(Long dishId, Long imageId) {
        dishes.get(dishId);
        DishImage_BIT242181 image = images.findById(imageId)
            .orElseThrow(() -> new NotFoundException("error.image.notfound"));
        if (!image.getDish().getId().equals(dishId)) throw new NotFoundException("error.image.notfound");
        return image;
    }
    private void validateUrl(String url) {
        if (url == null || url.isBlank() || url.length() > 2048)
            throw new BusinessException("validation.image.url");
        try {
            URI uri = URI.create(url.trim());
            if (uri.getHost() == null || !("https".equalsIgnoreCase(uri.getScheme())
                || "http".equalsIgnoreCase(uri.getScheme()))) {
                throw new IllegalArgumentException("Non-HTTP URL");
            }
        } catch (IllegalArgumentException e) {
            throw new BusinessException("validation.image.url");
        }
    }
}
