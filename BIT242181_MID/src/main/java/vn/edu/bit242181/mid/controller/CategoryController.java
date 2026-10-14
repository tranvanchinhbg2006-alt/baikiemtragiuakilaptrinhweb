package vn.edu.bit242181.mid.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.form.CategoryForm;
import vn.edu.bit242181.mid.model.DishCategory_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.repository.DishRepository;
import vn.edu.bit242181.mid.service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    private final DishRepository dishes;
    private final MessageSource messages;
    public CategoryController(CategoryService service, DishRepository dishes, MessageSource messages) {
        this.service = service;
        this.dishes = dishes;
        this.messages = messages;
    }
    private String msg(String key, HttpServletRequest req) {
        return messages.getMessage(key, null, RequestContextUtils.getLocale(req));
    }
    @GetMapping
    public String list(Model model) {
        var all = service.all();
        model.addAttribute("categories", all);
        var counts = new java.util.HashMap<Long, Long>();
        all.forEach(category -> counts.put(category.getId(), dishes.countByCategoryId(category.getId())));
        model.addAttribute("counts", counts);
        return "categories/list";
    }
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("form", new CategoryForm());
        model.addAttribute("editing", false);
        return "categories/form";
    }
    @PostMapping
    public String store(@Valid @ModelAttribute("form") CategoryForm form, BindingResult binding,
                        Model model, RedirectAttributes redirect, HttpServletRequest request) {
        if (binding.hasErrors()) return showForm(model, false, null);
        try {
            service.save(form, null);
            redirect.addFlashAttribute("success", msg("flash.category.created", request));
            return "redirect:/categories";
        } catch (BusinessException e) {
            binding.rejectValue("name", "duplicate", msg(e.getMessage(), request));
            return showForm(model, false, null);
        }
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.toForm(service.get(id)));
        return showForm(model, true, id);
    }
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") CategoryForm form,
                         BindingResult binding, Model model, RedirectAttributes redirect, HttpServletRequest request) {
        service.get(id);
        if (binding.hasErrors()) return showForm(model, true, id);
        try {
            service.save(form, id);
            redirect.addFlashAttribute("success", msg("flash.category.updated", request));
            return "redirect:/categories";
        } catch (BusinessException e) {
            binding.rejectValue("name", "duplicate", msg(e.getMessage(), request));
            return showForm(model, true, id);
        }
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect, HttpServletRequest request) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("success", msg("flash.category.deleted", request));
        } catch (BusinessException e) {
            redirect.addFlashAttribute("error", msg(e.getMessage(), request));
        }
        return "redirect:/categories";
    }
    private String showForm(Model model, boolean editing, Long id) {
        model.addAttribute("editing", editing);
        model.addAttribute("categoryId", id);
        return "categories/form";
    }
}
