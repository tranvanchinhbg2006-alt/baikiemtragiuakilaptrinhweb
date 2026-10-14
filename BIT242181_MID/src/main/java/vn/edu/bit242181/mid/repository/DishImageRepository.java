package vn.edu.bit242181.mid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.bit242181.mid.model.DishImage_BIT242181;

public interface DishImageRepository extends JpaRepository<DishImage_BIT242181, Long> {
    // Flush pending changes and evict stale objects after the bulk update.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update DishImage_BIT242181 image set image.thumbnail = false where image.dish.id = :dishId and image.thumbnail = true")
    int clearThumbnail(@Param("dishId") Long dishId);

    java.util.Optional<DishImage_BIT242181> findFirstByDishIdOrderByIdAsc(Long dishId);
}
