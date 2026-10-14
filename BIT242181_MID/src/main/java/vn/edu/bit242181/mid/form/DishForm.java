package vn.edu.bit242181.mid.form;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DishForm {
    @NotBlank(message = "{validation.name.required}")
    @Size(max = 160, message = "{validation.name.length}")
    private String name;

    @NotNull(message = "{validation.price.required}")
    @DecimalMin(value = "0.01", message = "{validation.price.positive}")
    @Digits(integer = 10, fraction = 2, message = "{validation.price.precision}")
    private BigDecimal price;

    @NotNull(message = "{validation.time.required}")
    @Min(value = 1, message = "{validation.time.positive}")
    private Integer preparationTime;

    private boolean available = true;

    @Size(max = 2000, message = "{validation.description.length}")
    private String description;

    @NotNull(message = "{validation.category.required}")
    private Long dishCategoryId;

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
    public Long getDishCategoryId() { return dishCategoryId; }
    public void setDishCategoryId(Long dishCategoryId) { this.dishCategoryId = dishCategoryId; }
}
