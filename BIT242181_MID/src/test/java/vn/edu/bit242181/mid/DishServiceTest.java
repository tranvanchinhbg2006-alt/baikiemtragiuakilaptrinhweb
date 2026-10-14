package vn.edu.bit242181.mid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.form.DishFilter;
import vn.edu.bit242181.mid.form.DishForm;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.repository.DishRepository;
import vn.edu.bit242181.mid.service.DishService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    @Mock DishRepository dishes;
    @Mock DishCategoryRepository categories;
    @InjectMocks DishService service;

    @Test void rejectsReversedPriceRange() {
        DishFilter filter = new DishFilter();
        filter.setMinPrice(new BigDecimal("100000"));
        filter.setMaxPrice(new BigDecimal("20000"));
        assertEquals("error.price.range", assertThrows(BusinessException.class, () -> service.search(filter)).getMessage());
        verifyNoInteractions(dishes);
    }

    @Test void rejectsNegativePriceRange() {
        DishFilter filter = new DishFilter();
        filter.setMinPrice(new BigDecimal("-1"));
        assertThrows(BusinessException.class, () -> service.search(filter));
    }

    @Test void usesDatabaseQueryAndPriceSort() {
        DishFilter filter = new DishFilter();
        filter.setKeyword("pho");
        filter.setAvailability("true");
        filter.setSort("priceDesc");
        when(dishes.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());
        assertEquals(0, service.search(filter).size());
        verify(dishes, times(1)).findAll(any(Specification.class), eq(Sort.by("price").descending().and(Sort.by("id"))));
    }

    @Test void rejectsDuplicateNameInSameCategory() {
        DishCategory_BIT242181 category = new DishCategory_BIT242181();
        category.setId(1L);
        when(categories.findById(1L)).thenReturn(Optional.of(category));
        when(dishes.existsByNameIgnoreCaseAndCategoryId("Pho", 1L)).thenReturn(true);
        DishForm form = sampleForm();
        assertEquals("validation.name.duplicate", assertThrows(BusinessException.class, () -> service.save(form, null)).getMessage());
        verify(dishes, never()).save(any(Dish_BIT242181.class));
    }

    @Test void rejectsNonExistingCategory() {
        when(categories.findById(1L)).thenReturn(Optional.empty());
        assertEquals("validation.category.invalid", assertThrows(BusinessException.class,
            () -> service.save(sampleForm(), null)).getMessage());
    }

    @Test void allowsSameNameInDifferentCategories() {
        DishCategory_BIT242181 category = new DishCategory_BIT242181();
        category.setId(1L);
        when(categories.findById(1L)).thenReturn(Optional.of(category));
        when(dishes.save(any(Dish_BIT242181.class))).thenAnswer(inv -> inv.getArgument(0));
        Dish_BIT242181 saved = service.save(sampleForm(), null);
        assertEquals("Pho", saved.getName());
        assertSame(category, saved.getCategory());
    }

    private DishForm sampleForm() {
        DishForm form = new DishForm();
        form.setName(" Pho ");
        form.setPrice(new BigDecimal("55000.00"));
        form.setPreparationTime(18);
        form.setDishCategoryId(1L);
        return form;
    }
}
