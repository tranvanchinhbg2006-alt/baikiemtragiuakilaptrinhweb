package vn.edu.bit242181.mid.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.form.DishFilter;
import vn.edu.bit242181.mid.form.DishForm;
import vn.edu.bit242181.mid.model.Dish_BIT242181;
import vn.edu.bit242181.mid.repository.DishCategoryRepository;
import vn.edu.bit242181.mid.service.DishService;
import java.util.Locale;

@Controller
@RequestMapping("/dishes")
public class DishController {
    private final DishService service;
    private final DishCategoryRepository categories;
    private final MessageSource messages;
    public DishController(DishService service, DishCategoryRepository categories, MessageSource messages) {
        this.service = service;
        this.categories = categories;
        this.messages = messages;
    }
    private String msg(String key, HttpServletRequest request) {
        return messages.getMessage(key, null, RequestContextUtils.getLocale(request));
    }
    private void formData(Model model) { model.addAttribute("categories", categories.findAll(org.springframework.data.domain.Sort.by("name"))); }

    @GetMapping
    public String list(@ModelAttribute("filter") DishFilter filter, BindingResult binding, Model model, HttpServletRequest request) {
        formData(model);
        if (binding.hasErrors()) {
            model.addAttribute("filterError", msg("error.price.invalid", request));
            model.addAttribute("dishes", java.util.List.of());
        } else {
            try { model.addAttribute("dishes", service.search(filter)); }
            catch (BusinessException e) {
                model.addAttribute("filterError", msg(e.getMessage(), request));
                model.addAttribute("dishes", java.util.List.of());
            }
        }
        return "dishes/list";
    }
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("form", new DishForm());
        model.addAttribute("editing", false);
        formData(model);
        return "dishes/form";
    }
    @PostMapping
    public String store(@Valid @ModelAttribute("form") DishForm form, BindingResult binding,
                        Model model, RedirectAttributes redirect, HttpServletRequest request) {
        if (binding.hasErrors()) return invalidForm(model, false);
        try {
            Dish_BIT242181 saved = service.save(form, null);
            redirect.addFlashAttribute("success", msg("flash.dish.created", request));
            return "redirect:/dishes/" + saved.getId();
        } catch (BusinessException e) {
            binding.rejectValue(e.getMessage().equals("validation.name.duplicate") ? "name" : "dishCategoryId",
                "invalid", msg(e.getMessage(), request));
            return invalidForm(model, false);
        }
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("dish", service.get(id));
        return "dishes/detail";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.toForm(service.get(id)));
        model.addAttribute("dishId", id);
        model.addAttribute("editing", true);
        formData(model);
        return "dishes/form";
    }
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") DishForm form,
                         BindingResult binding, Model model, RedirectAttributes redirect, HttpServletRequest request) {
        service.get(id); // Non-existent ID returns HTTP 404 even if the form is invalid.
        if (binding.hasErrors()) return invalidEdit(model, id);
        try {
            service.save(form, id);
            redirect.addFlashAttribute("success", msg("flash.dish.updated", request));
            return "redirect:/dishes/" + id;
        } catch (BusinessException e) {
            binding.rejectValue(e.getMessage().equals("validation.name.duplicate") ? "name" : "dishCategoryId",
                "invalid", msg(e.getMessage(), request));
            return invalidEdit(model, id);
        }
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect, HttpServletRequest request) {
        service.delete(id);
        redirect.addFlashAttribute("success", msg("flash.dish.deleted", request));
        return "redirect:/dishes";
    }
    private String invalidForm(Model model, boolean editing) {
        model.addAttribute("editing", editing);
        formData(model);
        return "dishes/form";
    }
    private String invalidEdit(Model model, Long id) {
        model.addAttribute("dishId", id);
        return invalidForm(model, true);
    }
}
