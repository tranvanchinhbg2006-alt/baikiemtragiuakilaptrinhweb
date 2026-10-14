package vn.edu.bit242181.mid.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.exception.NotFoundException;
import vn.edu.bit242181.mid.form.CategoryForm;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.repository.DishRepository;
import java.util.List;

@Service
public class CategoryService {
    private final DishCategoryRepository categories;
    private final DishRepository dishes;
    public CategoryService(DishCategoryRepository categories, DishRepository dishes) {
        this.categories = categories;
        this.dishes = dishes;
    }
    @Transactional(readOnly = true)
    public List<DishCategory_BIT242181> all() { return categories.findAll(Sort.by("name")); }
    @Transactional(readOnly = true)
    public DishCategory_BIT242181 get(Long id) {
        return categories.findById(id).orElseThrow(() -> new NotFoundException("error.category.notfound"));
    }
    @Transactional
    public DishCategory_BIT242181 save(CategoryForm form, Long id) {
        String name = form.getName().trim();
        boolean duplicate = id == null ? categories.existsByNameIgnoreCase(name)
            : categories.existsByNameIgnoreCaseAndIdNot(name, id);
        if (duplicate) throw new BusinessException("validation.category.duplicate");
        DishCategory_BIT242181 category = id == null ? new DishCategory_BIT242181() : get(id);
        category.setName(name);
        category.setDescription(form.getDescription() == null ? "" : form.getDescription().trim());
        return categories.save(category);
    }
    @Transactional
    public void delete(Long id) {
        DishCategory_BIT242181 category = get(id);
        if (dishes.existsByCategoryId(id)) throw new BusinessException("error.category.inuse");
        categories.delete(category);
        categories.flush();
    }
    public CategoryForm toForm(DishCategory_BIT242181 category) {
        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        form.setDescription(category.getDescription());
        return form;
    }
    public long countDishes(Long categoryId) { return dishes.countByCategoryId(categoryId); }
}
