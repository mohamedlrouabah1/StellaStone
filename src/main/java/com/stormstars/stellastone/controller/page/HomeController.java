package com.stormstars.stellastone.controller.page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormstars.stellastone.controller.user.UserController;
import com.stormstars.stellastone.model.atelier.Fusee;
import com.stormstars.stellastone.service.user.UserService;

@Controller
public class HomeController {
    @Autowired
    private UserService service;

    @GetMapping(value = "/home")
    public String homePage(Model model, @RequestParam(value = "error", defaultValue = "no-error") String error) {
        Long userId = UserController.getCurrentUser().getId();
        model.addAttribute("user", service.findById(userId));
        List<Fusee> fusees = service.findById(userId).getFusees();
        model.addAttribute("lesfuseesdisponible", fusees);
        if (!error.equals("no-error"))
            model.addAttribute("error", "You Already have a project with the same name");

        return "home";
    }

    @GetMapping("/home/fusee/{id}")
    public String getFusee(@PathVariable Long id, Model model) {
        if (id == 5) 
                return "animation/spacex";
        else if (id == 6)
                return "animation/nova";
        else
                return "animation/rocket";
    }

}
