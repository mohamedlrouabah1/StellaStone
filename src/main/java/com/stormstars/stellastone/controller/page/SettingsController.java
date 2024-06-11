package com.stormstars.stellastone.controller.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormstars.stellastone.controller.user.UserController;
import com.stormstars.stellastone.service.user.UserService;

@Controller
public class SettingsController {
    @Autowired
    private UserService service;

    @GetMapping(value = "/home/settings")
    public String settingPage(Model model, @RequestParam(value = "error", defaultValue = "no-error") String error) {

        Long userId = UserController.getCurrentUser().getId();
        model.addAttribute("user", service.findById(userId));
        if (error.equals("password")) {
            model.addAttribute("error", "Passwords are not matching or Wrong previous password!");
        } else if (error.equals("username")) {
            model.addAttribute("error", "Username already used.");
        }

        return "settings";
    }

    
}
