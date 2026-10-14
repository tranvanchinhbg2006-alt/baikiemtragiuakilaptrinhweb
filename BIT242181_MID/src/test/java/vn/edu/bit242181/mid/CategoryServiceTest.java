package vn.edu.bit242181.mid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.repository.DishRepository;
import vn.edu.bit242181.mid.service.CategoryService;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock DishCategoryRepository categories;
    @Mock DishRepository dishes;
    @InjectMocks CategoryService service;

    @Test void cannotDeleteCategoryInUse() {
        DishCategory_BIT242181 category = new DishCategory_BIT242181();
        category.setId(5L);
        when(categories.findById(5L)).thenReturn(Optional.of(category));
        when(dishes.existsByCategoryId(5L)).thenReturn(true);
        assertEquals("error.category.inuse", assertThrows(BusinessException.class, () -> service.delete(5L)).getMessage());
        verify(categories, never()).delete(any());
    }
}
