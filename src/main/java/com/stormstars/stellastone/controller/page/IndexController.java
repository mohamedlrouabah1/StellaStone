package com.stormstars.stellastone.controller.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormstars.stellastone.service.user.UserService;


@Controller
public class IndexController {

    @Autowired
    UserService userService;

    /**
     * @param request  : provide request information for HTTP servlets
     * @param response : provide HTTP-specific functionality in sending a response
     * @return String : return the index file
     */
    @GetMapping(value = { "/", "/index" })
    public String indexPage(HttpServletRequest request, HttpServletResponse response) {
        userService.autologout(request, response);

        return "index";
    }

    /**
     * @return String : redirect to the index page
     */
    @GetMapping(value = "/home/logout")
    private String logoutPage() {
        return "redirect:/";
    }

    /**
     * @return String : return the signin file
     */
    @GetMapping(value = "/signIn")
    private String signInPage() {
        return "authentification/signIn";
    }

    /**
     * @param model   : holder for model attributes
     * @param "error" : error (RequestParam)
     * @param error   : Boolean for error parameter
     * @return String : return the login file
     */
    @GetMapping(value = "/logIn")
    private String logInPage(Model model, @RequestParam(value = "error", defaultValue = "false") Boolean error) {
        if (error)
            model.addAttribute("error", "Username and password invalid.");

        return "authentification/logIn";
    }
}
