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
    public String showGrades(Model model) {
        loginDisplay(model);
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

    @PostMapping("/{id}/delete")
    public String deleteTeam(@PathVariable long id, Model model) {

        if(gradeService.getGrade(id)!=null) {
           gradeService.deleteGrade(id);
            loginDisplay(model);
            return "index";
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
