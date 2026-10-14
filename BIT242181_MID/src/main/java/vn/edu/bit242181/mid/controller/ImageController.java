package vn.edu.bit242181.mid.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import vn.edu.bit242181.mid.exception.BusinessException;
import vn.edu.bit242181.mid.service.ImageService;

@Controller
@RequestMapping("/dishes/{dishId}/images")
public class ImageController {
    private final ImageService service;
    private final MessageSource messages;
    public ImageController(ImageService service, MessageSource messages) {
        this.service = service;
        this.messages = messages;
    }
    private void flash(RedirectAttributes r, String type, String key, HttpServletRequest req) {
        r.addFlashAttribute(type, messages.getMessage(key, null, RequestContextUtils.getLocale(req)));
    }
    @PostMapping
    public String add(@PathVariable Long dishId, @RequestParam(required = false) String imageUrl,
                      @RequestParam(defaultValue = "false") boolean thumbnail,
                      RedirectAttributes r, HttpServletRequest req) {
        try {
            service.add(dishId, imageUrl, thumbnail);
            flash(r, "success", "flash.image.added", req);
        } catch (BusinessException e) { flash(r, "error", e.getMessage(), req); }
        return "redirect:/dishes/" + dishId;
    }
    @PostMapping("/{imageId}/thumbnail")
    public String thumbnail(@PathVariable Long dishId, @PathVariable Long imageId,
                            RedirectAttributes r, HttpServletRequest req) {
        service.makeThumbnail(dishId, imageId);
        flash(r, "success", "flash.image.thumbnail", req);
        return "redirect:/dishes/" + dishId;
    }
    @PostMapping("/{imageId}/delete")
    public String delete(@PathVariable Long dishId, @PathVariable Long imageId,
                         RedirectAttributes r, HttpServletRequest req) {
        service.delete(dishId, imageId);
        flash(r, "success", "flash.image.deleted", req);
        return "redirect:/dishes/" + dishId;
    }
}
