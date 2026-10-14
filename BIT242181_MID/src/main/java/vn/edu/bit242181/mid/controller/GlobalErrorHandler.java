package vn.edu.bit242181.mid.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.exception.NotFoundException;

@ControllerAdvice
public class GlobalErrorHandler {
    private final MessageSource messages;
    public GlobalErrorHandler(MessageSource messages) { this.messages = messages; }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException e, Model model, HttpServletRequest request) {
        model.addAttribute("errorTitle", messages.getMessage("error.notfound.title", null, RequestContextUtils.getLocale(request)));
        model.addAttribute("errorMessage", messages.getMessage(e.getMessage(), null, RequestContextUtils.getLocale(request)));
        return "error-page";
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String businessError(BusinessException e, Model model, HttpServletRequest request) {
        model.addAttribute("errorTitle", messages.getMessage("error.input.title", null, RequestContextUtils.getLocale(request)));
        model.addAttribute("errorMessage", messages.getMessage(e.getMessage(), null, RequestContextUtils.getLocale(request)));
        return "error-page";
    }
}
