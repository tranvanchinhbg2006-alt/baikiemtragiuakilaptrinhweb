package vn.edu.bit242181.mid.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BIT242181_DishCategories")
public class DishCategory_BIT242181 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT242181_Id")
    private Long id;

    @Column(name = "BIT242181_Name", nullable = false, length = 120)
    private String name;

    @Column(name = "BIT242181_Description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Dish_BIT242181> dishes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Dish_BIT242181> getDishes() { return dishes; }
}
