package vn.edu.bit242181.mid.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryForm {
    @NotBlank(message = "{validation.name.required}")
    @Size(max = 120, message = "{validation.category.length}")
    private String name;
    @Size(max = 1000, message = "{validation.category.description.length}")
    private String description;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
