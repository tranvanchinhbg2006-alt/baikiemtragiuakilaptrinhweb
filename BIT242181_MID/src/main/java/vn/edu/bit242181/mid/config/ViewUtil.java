package vn.edu.bit242181.mid.config;

import org.springframework.stereotype.Component;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import vn.edu.bit242181.mid.model.DishImage_BIT242181;

@Component("dishView")
public class ViewUtil {
    public String thumbnail(Dish_BIT242181 dish) {
        return dish.getImages().stream().filter(DishImage_BIT242181::isThumbnail)
            .findFirst().or(() -> dish.getImages().stream().findFirst())
            .map(DishImage_BIT242181::getImageUrl).orElse(null);
    }
    public boolean isQuick(Dish_BIT242181 dish) { return dish.getPreparationTime() <= 15; }
}
