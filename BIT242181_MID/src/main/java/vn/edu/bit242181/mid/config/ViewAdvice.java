package vn.edu.bit242181.mid.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

@ControllerAdvice
public class ViewAdvice {
    private final MessageSource messages;
    public ViewAdvice(MessageSource messages) { this.messages = messages; }
    @ModelAttribute
    public void navigation(Model model, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        model.addAttribute("lang", locale.getLanguage().equals("vi") ? "vi" : "en");
        String current = request.getRequestURI();
        if (request.getQueryString() != null) current += "?" + request.getQueryString();
        model.addAttribute("returnTo", current);
    }
    public String translate(String key) { return messages.getMessage(key, null, LocaleContextHolder.getLocale()); }
}
