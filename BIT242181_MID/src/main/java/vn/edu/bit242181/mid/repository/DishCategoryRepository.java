package vn.edu.bit242181.mid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;

public interface DishCategoryRepository extends JpaRepository<DishCategory_BIT242181, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
