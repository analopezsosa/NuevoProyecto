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
        User aux = userService.getUser(user);
        if(aux.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }
        model.addAttribute("grades",gradeService.gradeList());
        return "viewgrades";
    }


    @GetMapping("/create")
    public String createGrade(Model model,@RequestParam String name,@RequestParam int gradeNumber,@RequestParam String teacher){
        loginDisplay(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (userService.getUser(username)==null){

            model.addAttribute("error",true);
            return "creategrade";
        }
        Grade grade = new Grade(name,gradeNumber,teacher);
        gradeService.saveGrade(grade);
        model.addAttribute("grade",grade);
        return "index";
    }

    @GetMapping("/join")
    public String showJoin(Model model){
        loginDisplay(model);
        return "joingrade";
    }

    @PostMapping("/join")
    public String joinGrade(@RequestParam long id, Model model){
        loginDisplay(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Grade grade= gradeService.getGrade(id);

        User newMember;
        if (username != null) {
            newMember = userService.getUser(username);
        } else {
            newMember = null;
        }
        if (newMember==null) {
            model.addAttribute("notRegistered",true);
            return "signup";
        } else if(grade==null) {
            model.addAttribute("notRegistered",true);
            return "creategrade";
        }

        else {
            model.addAttribute("grade", gradeService.getGrade(id));
            newMember.setGrade(grade);
            userService.addUser(newMember);
            gradeService.addGrade(grade);
            return "grade";
        }
    }
    @GetMapping("/{id}")
    public String showGrade(Model model,@PathVariable long id) {
        System.out.println("si ve el cursp");
        Grade exist = gradeService.getGrade(id);
        if (exist != null) {
            model.addAttribute("grade", gradeService.getGrade(id));
            //loginDisplay(model);
            return "grade";
        }
        return "error";
    }

    @PostMapping("/removegrade")
    public String removegrade(@PathVariable long id, Model model) {

        if(gradeService.getGrade(id)!=null) {
            removeUsers(id);
            gradeService.deleteGrade(id);
            loginDisplay(model);
            model.addAttribute("admin",true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
            model.addAttribute("isLogged",true); //esto igual
            model.addAttribute("grades",gradeService.gradeList());
            return "viewgrades";
        }
        return "error";
    }

    public void removeUsers(Long id){
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
    public String edited(Model model,@RequestParam long id, @RequestParam String name, @RequestParam int gradeNumber, @RequestParam String teacher){
        Grade editThisGrade = gradeService.getGrade(id);

        editThisGrade.setName(name);
        editThisGrade.setGradeNumber(gradeNumber);
        editThisGrade.setTeacher(teacher);
        gradeService.addGrade(editThisGrade);
        loginDisplay(model);
        model.addAttribute("admin",true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
        model.addAttribute("isLogged",true); //esto igual
        model.addAttribute("grades",gradeService.gradeList());
        return "viewgrades";
    }


    @PostMapping("/removeuserfromgrade")
    public String removeUserFromGrade(Model model, @RequestParam String name,@RequestParam long id){
        User user=userService.getUser(name);
        Grade grade=gradeService.getGrade(id);
        user.deleteGrade(grade);
        grade.deleteUser(user);
        gradeService.addGrade(grade);
        loginDisplay(model);
        model.addAttribute("admin",true); //esto no se muy bien si hay necesidad de volver a indicar si es admin o lo guarda
        model.addAttribute("isLogged",true); //esto igual
        model.addAttribute("grades",gradeService.gradeList());
        return "viewgrades";
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
