package vn.edu.bit242181.mid.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.exception.NotFoundException;
import vn.edu.bit242181.mid.form.DishFilter;
import vn.edu.bit242181.mid.form.DishForm;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.repository.DishRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class DishService {
    private final DishRepository dishes;
    private final DishCategoryRepository categories;

    public DishService(DishRepository dishes, DishCategoryRepository categories) {
        this.dishes = dishes;
        this.categories = categories;
    }

    @Transactional(readOnly = true)
    public List<Dish_BIT242181> search(DishFilter filter) {
        if (filter.getMinPrice() != null && filter.getMinPrice().compareTo(BigDecimal.ZERO) < 0
            || filter.getMaxPrice() != null && filter.getMaxPrice().compareTo(BigDecimal.ZERO) < 0
            || filter.getMinPrice() != null && filter.getMaxPrice() != null
               && filter.getMinPrice().compareTo(filter.getMaxPrice()) > 0) {
            throw new BusinessException("error.price.range");
        }

        Specification<Dish_BIT242181> spec = (root, query, cb) -> cb.conjunction();
        if (StringUtils.hasText(filter.getKeyword())) {
            String keyword = "%" + filter.getKeyword().trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), keyword, '\\'));
        }
        if (filter.getCategoryId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), filter.getCategoryId()));
        }
        if ("true".equals(filter.getAvailability()) || "false".equals(filter.getAvailability())) {
            boolean available = Boolean.parseBoolean(filter.getAvailability());
            spec = spec.and((root, query, cb) -> cb.equal(root.get("available"), available));
        }
        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }
        Sort sort = switch (filter.getSort() == null ? "name" : filter.getSort()) {
            case "priceAsc" -> Sort.by("price").ascending().and(Sort.by("id"));
            case "priceDesc" -> Sort.by("price").descending().and(Sort.by("id"));
            case "timeAsc" -> Sort.by("preparationTime").ascending().and(Sort.by("id"));
            default -> Sort.by("name").ascending().and(Sort.by("id"));
        };
        // WHERE and ORDER BY are sent to PostgreSQL by Spring Data JPA.
        // No in-memory filtering or sorting.
        return dishes.findAll(spec, sort);
    }

    @Transactional(readOnly = true)
    public Dish_BIT242181 get(Long id) {
        return dishes.findById(id).orElseThrow(() -> new NotFoundException("error.dish.notfound"));
    }

    @Transactional
    public Dish_BIT242181 save(DishForm form, Long currentId) {
        DishCategory_BIT242181 category = categories.findById(form.getDishCategoryId())
            .orElseThrow(() -> new BusinessException("validation.category.invalid"));
        String name = form.getName().trim();
        boolean duplicate = currentId == null
            ? dishes.existsByNameIgnoreCaseAndCategoryId(name, category.getId())
            : dishes.existsByNameIgnoreCaseAndCategoryIdAndIdNot(name, category.getId(), currentId);
        if (duplicate) throw new BusinessException("validation.name.duplicate");
        Dish_BIT242181 dish = currentId == null ? new Dish_BIT242181() : get(currentId);
        dish.setName(name);
        dish.setPrice(form.getPrice());
        dish.setPreparationTime(form.getPreparationTime());
        dish.setAvailable(form.isAvailable());
        dish.setDescription(form.getDescription() == null ? "" : form.getDescription().trim());
        dish.setCategory(category);
        return dishes.save(dish);
    }

    @Transactional
    public void delete(Long id) {
        dishes.delete(get(id));
        dishes.flush();
    }

    public DishForm toForm(Dish_BIT242181 dish) {
        DishForm form = new DishForm();
        form.setName(dish.getName());
        form.setPrice(dish.getPrice());
        form.setPreparationTime(dish.getPreparationTime());
        form.setAvailable(dish.isAvailable());
        form.setDescription(dish.getDescription());
        form.setDishCategoryId(dish.getCategory().getId());
        return form;
    }
}
