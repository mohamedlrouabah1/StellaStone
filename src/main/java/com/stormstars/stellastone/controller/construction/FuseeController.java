package com.stormstars.stellastone.controller.construction;

import java.io.IOException;
import java.security.Principal;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stormstars.stellastone.controller.user.UserController;
import com.stormstars.stellastone.model.atelier.Fusee;
import com.stormstars.stellastone.model.user.User;
import com.stormstars.stellastone.service.construction.ConstructionService;
import com.stormstars.stellastone.service.user.UserService;

@Controller
public class FuseeController {

    @Autowired
    ConstructionService fuseeService;

    @Autowired
    UserService userService;

    private static Fusee fusee;

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
     * @param id : Fusee id property
     * @return byte[] : Fusee avatar
     */
    @ResponseBody
    @GetMapping(value = "/home/modeRealiste/fusee/{id}/icon")
    public byte[] getLogos(@PathVariable Long id) {
        return fuseeService.findById(id).getImage();
    }

    public FuseeController(ConstructionService fuseeService) {
        this.fuseeService = fuseeService;
    }

    /************************************************/
    /**
     * @param u
     */
    public static void setCurrentFusee(Fusee u) {
        fusee = u;
    }

    /**
     * @param model
     * @return
     */
    @GetMapping(value = "/home/modeRealiste")
    private String modeRealiste(Model model, Fusee f) {
        User user = UserController.getCurrentUser();
        List<Fusee> fusees = user.getFusees(); // Initialize fusees collection
        model.addAttribute("userFusee", fusees);
        model.addAttribute("allFusee", fuseeService.findAll());
        return "/construction/modeRealiste";
    }

    /**
     * @return String : return the modeAventure file
     */
    @GetMapping(value = "/home/modeAventure")
    public String modeAventure() {
        return "/construction/modeAventure";
    }

    /**
     * @return
     */
    public static Fusee getCurrentFusee() {
        return fusee;
    }

    @PostMapping("/home/modeRealiste/equiperfusee")
    public String equiperFusee(@RequestParam("idFusee") Long idFusee, Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());
        Fusee currentFusee = fuseeService.findFuseebyUserId(user.getId());

        if (user.getFusees() != null) {
            user.getFusees().clear();
        }
        if (currentFusee != null) {
            currentFusee.setUser(null);
        }
        Fusee newFusee = fuseeService.findById(idFusee);
        newFusee.setUser(user);

        user.getFusees().add(newFusee);
        fuseeService.update(newFusee);
        userService.update(user);
        return "redirect:/home";
    }

    @PostMapping("/home/modeRealiste/addFusee")
    public String addFusee(@RequestParam("nomFusee") String nomFusee,
            @RequestParam("infos") String infos,
            @RequestParam("propriete") String propriete,
            @RequestParam("historique") String historique,
            @RequestParam("image") MultipartFile image) throws IOException {
        Fusee fusee = new Fusee();
        fusee.setNomFusee(nomFusee);
        fusee.setInfos(infos);
        fusee.setPropriete(propriete);
        fusee.setHistorique(historique);
        fusee.setImage(image.getBytes());
        fuseeService.update(fusee);

        return "redirect:/home/modeRealiste";
    }

}
