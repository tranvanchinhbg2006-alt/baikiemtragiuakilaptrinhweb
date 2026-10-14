package vn.edu.bit242181.mid.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;

@Controller
public class LocaleController {
    private final LocaleResolver resolver;
    public LocaleController(LocaleResolver resolver) { this.resolver = resolver; }
    @GetMapping("/language")
    public String language(@RequestParam(defaultValue = "en") String lang,
                           @RequestParam(defaultValue = "/dishes") String returnTo,
                           HttpServletRequest request, HttpServletResponse response) {
        resolver.setLocale(request, response, "vi".equalsIgnoreCase(lang) ? new Locale("vi") : Locale.ENGLISH);
        // Only local relative paths are allowed: prevent open-redirects.
        if (!returnTo.startsWith("/") || returnTo.startsWith("//") || returnTo.contains("\\")
            || returnTo.contains("\r") || returnTo.contains("\n")) returnTo = "/dishes";
        return "redirect:" + returnTo;
    }
    @GetMapping("/")
    public String home() { return "redirect:/dishes"; }
}
