package vn.edu.bit242181.mid.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BIT242181_Dishes")
public class Dish_BIT242181 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT242181_Id")
    private Long id;

    @Column(name = "BIT242181_Name", nullable = false, length = 160)
    private String name;

    @Column(name = "BIT242181_Price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "BIT242181_PreparationTime", nullable = false)
    private Integer preparationTime;

    @Column(name = "BIT242181_IsAvailable", nullable = false)
    private boolean available = true;

    @Column(name = "BIT242181_Description", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT242181_DishCategoryId", referencedColumnName = "BIT242181_Id", nullable = false)
    private DishCategory_BIT242181 category;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<DishImage_BIT242181> images = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public DishCategory_BIT242181 getCategory() { return category; }
    public void setCategory(DishCategory_BIT242181 category) { this.category = category; }
    public List<DishImage_BIT242181> getImages() { return images; }
}
