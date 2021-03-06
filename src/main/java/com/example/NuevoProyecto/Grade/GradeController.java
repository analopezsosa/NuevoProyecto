package com.example.NuevoProyecto.Grade;


import com.example.NuevoProyecto.User.User;
import com.example.NuevoProyecto.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String showGrades(Model model, HttpSession session) {
        String user = (String)session.getAttribute("user");
        //loginDisplay(model);
        if (user!=null) {
            User aux = userService.getUser(user);

            if (aux.getRoles().contains("ADMIN")) {
                model.addAttribute("admin", true);
            }
        }else {
            model.addAttribute("notRegistered",true);
        }
        model.addAttribute("grades",gradeService.gradeList());
        return "viewgrades";
    }

    @PostMapping("/create")
    public String createGrade(Model model,@RequestParam String name,@RequestParam int gradeNumber,@RequestParam String teacher, HttpSession session){
        String infoname = ( String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
            Grade grade = new Grade(name,gradeNumber,teacher);
            gradeService.addGrade(grade);
            model.addAttribute("grades",gradeService.gradeList());
            return "viewgrades";
        }
        return "error";
    }

    @GetMapping("/{id}")
    public String showGrade(Model model,@PathVariable long id, HttpSession session) {
        String infoname = (String) session.getAttribute("user");

        if (infoname != null) {
            User u = userService.getUser(infoname);
            Grade exist = gradeService.getGrade(id);
            if (exist != null) {
                model.addAttribute("grade", gradeService.getGrade(id));
                if (u.getRoles().contains("ADMIN")) {
                    model.addAttribute("admin", true);
                }
                return "grade";
            }
        } else {
            model.addAttribute("notRegistered",true);
            Grade exist = gradeService.getGrade(id);
            if (exist != null) {
                model.addAttribute("grade", gradeService.getGrade(id));
                return "grade";
            }
        }
            return "error";

    }

    @PostMapping("/removegrade")
    public String removegrade(@RequestParam long id, Model model, HttpSession session) {
        String info = (String)session.getAttribute("user");
        User aux = userService.getUser(info);
        if(aux.getRoles().contains("ADMIN")){
            if(gradeService.getGrade(id)!=null) {
                removeUsers(id);
                gradeService.deleteGrade(id);
                loginDisplay(model);
                model.addAttribute("admin",true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
                model.addAttribute("isLogged",true); //esto igual
                model.addAttribute("grades",gradeService.gradeList());
                return "viewgrades";
            }
        }

        return "error";
    }

    public void removeUsers( Long id){
        Grade grade=gradeService.getGrade(id);
        List<User> userToDeleteFromGrade=grade.getUserList();
        int x=userToDeleteFromGrade.size();
        for (int i=0;i<x;i++) {
            userToDeleteFromGrade.get(i).deleteGrade(grade);
        }
        grade.deleteUserList(userToDeleteFromGrade);
        gradeService.addGrade(grade);

    }


    @PostMapping("/editgrade")
    public String edited(Model model,@RequestParam long id, @RequestParam String name, @RequestParam int gradeNumber, @RequestParam String teacher, HttpSession session){
        String info = (String)session.getAttribute("user");
        User aux = userService.getUser(info);
        if(aux.getRoles().contains("ADMIN")) {
            Grade editThisGrade = gradeService.getGrade(id);

            editThisGrade.setName(name);
            editThisGrade.setGradeNumber(gradeNumber);
            editThisGrade.setTeacher(teacher);
            gradeService.addGrade(editThisGrade);

            model.addAttribute("admin", true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
            model.addAttribute("isLogged", true); //esto igual
            model.addAttribute("grades", gradeService.gradeList());
            return "viewgrades";
        }
        return "error";
    }


    @PostMapping("/removeuserfromgrade")
    public String removeUserFromGrade(Model model, @RequestParam String name,@RequestParam long id,HttpSession session){
        String info = (String)session.getAttribute("user");
        User aux = userService.getUser(info);
        if(aux.getRoles().contains("ADMIN")) {

            User user = userService.getUser(name);
            Grade grade = gradeService.getGrade(id);
            user.deleteGrade(grade);
            grade.deleteUser(user);
            gradeService.addGrade(grade);

            model.addAttribute("admin", true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
            model.addAttribute("isLogged", true); //esto igual
            model.addAttribute("grades", gradeService.gradeList());
            return "viewgrades";
        }
        return "error";
    }


    private void loginDisplay(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            model.addAttribute("isLogged", true);
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("admin", true);
            } else {
                model.addAttribute("username", userService.getUser(auth.getName()));
            }
        }
    }

}
