package com.stormstars.stellastone.controller.user;

import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.stormstars.stellastone.model.user.User;
import com.stormstars.stellastone.service.user.UserService;


@Controller
public class UserController {

    @Autowired
    private UserService service;

    private static User user;

    /**
     * @param binder
     * @throws ServletException
     */
    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) throws ServletException {

        // Convert multipart object to byte[]
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    /**
     * @param id : user id property
     * @return byte[] : user avatar
     */
    @ResponseBody
    @GetMapping(value = "/home/settings/user/{id}/avatar")
    private byte[] getImageAvatar(@PathVariable Long id) {
        return service.findById(id).getAvatar();
    }
    /**
     * @param model : holder for model attributes
     * @param u     : User object
     * @return String : redirect to PageController
     */
    @PostMapping(value = "/signIn")
    public String signIn(Model model, User u) {

        String pwd = u.getPassword();

        if (service.isUserRegistered(u)) {
            model.addAttribute("error", "Username already used.");
            return "authentification/signIn";
        }

        service.save(u);
        setCurrentUser(u);

        service.autologin(u.getUsername(), pwd);

        return "redirect:/home";
    }

    /**
     * @param u : User object
     * @return String : redirect to PageController
     */
    @PostMapping(value = "/home/settings/update-account")
    private String updateAccount(User u) {

        u.setId(getCurrentUser().getId());
        u.setAvatar(getCurrentUser().getAvatar());
        u.setPassword(getCurrentUser().getPassword());
        u.setUsername(getCurrentUser().getUsername());

        setCurrentUser(u);

        service.update(u);

        return "redirect:/home/settings";
    }

     /**
     * @param avatar : The uploaded avatar
     * @return String : redirect to the pageController
     */
    @PostMapping(value = "/home/settings/update-avatar")
    private String updateAvatar(byte[] avatar) {

        if (avatar.length == 0)
            return "redirect:/home/settings?error=avatar";

        User u = UserController.getCurrentUser();
        u.setAvatar(avatar);
        service.update(u);

        UserController.setCurrentUser(service.findById(u.getId()));

        return "redirect:/home/settings";
    }
    /**
     * @param model  : holder for model attributes
     * @param oldPwd : the old password
     * @param pwd1   : the new password
     * @param pwd2   : the confirmation of the password
     * @return String : redirect to PageController
     */
    @PostMapping(value = "/home/settings/update-password")
    public String updatePassword(Model model, String oldPwd, String pwd1, String pwd2) {

        // UPDATE THE PASSWORD
        String pwd;
        if (service.canUpdatePassword(getCurrentUser().getId(), oldPwd, pwd1, pwd2))
            pwd = service.encodePassword(pwd2);
        else if (oldPwd.equals(""))
            pwd = getCurrentUser().getPassword();
        else
            return "redirect:/home/settings?error=password";

        getCurrentUser().setPassword(pwd);
        service.update(getCurrentUser());

        return "redirect:/home/settings";
    }

    /**
     * @param username : User username property
     * @return String : reditrect to PageController
     */
    @PostMapping(value = "/home/settings/update-username")
    private String updateUsername(String username) {
        if (!service.canUpdate(username, getCurrentUser()))
            return "redirect:/home/settings?error=username";

        getCurrentUser().setUsername(username);
        service.update(getCurrentUser());

        return "redirect:/home/settings";
    }

    /**
     * @return String : redirect to PageController
     */
    @PostMapping(value = "/home/settings/delete-account")
    private String deleteAccount() {
        service.delete(getCurrentUser());
        return "redirect:/";
    }

    

    @GetMapping("/home/search")
    @ResponseBody
    public List<User> searchUsers(@RequestParam("q") String query) {
        return service.search(query);
      }
    
    /**
     * @param u : User object
     */
    public static void setCurrentUser(User u) {
        user = u;
    }

    /**
     * @return User object
     */
    public static User getCurrentUser() {
        return user;
    }
}