package vn.edu.bit242181.mid.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BIT242181_DishImages")
public class DishImage_BIT242181 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT242181_Id")
    private Long id;

    @Column(name = "BIT242181_ImageUrl", nullable = false, length = 2048)
    private String imageUrl;

    @Column(name = "BIT242181_IsThumbnail", nullable = false)
    private boolean thumbnail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT242181_DishId", referencedColumnName = "BIT242181_Id", nullable = false)
    private Dish_BIT242181 dish;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isThumbnail() { return thumbnail; }
    public void setThumbnail(boolean thumbnail) { this.thumbnail = thumbnail; }
    public Dish_BIT242181 getDish() { return dish; }
    public void setDish(Dish_BIT242181 dish) { this.dish = dish; }
}
