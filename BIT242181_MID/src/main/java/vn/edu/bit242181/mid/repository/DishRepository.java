package vn.edu.bit242181.mid.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish_BIT242181, Long>, JpaSpecificationExecutor<Dish_BIT242181> {
    @Override
    @EntityGraph(attributePaths = {"category", "images"})
    List<Dish_BIT242181> findAll(Specification<Dish_BIT242181> spec, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Dish_BIT242181> findById(Long id);

    boolean existsByCategoryId(Long categoryId);
    long countByCategoryId(Long categoryId);
    boolean existsByNameIgnoreCaseAndCategoryId(String name, Long categoryId);
    boolean existsByNameIgnoreCaseAndCategoryIdAndIdNot(String name, Long categoryId, Long id);
}
