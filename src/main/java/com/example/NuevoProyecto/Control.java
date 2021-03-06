package com.example.NuevoProyecto;


import com.example.NuevoProyecto.Grade.Grade;
import com.example.NuevoProyecto.Grade.GradeService;
import com.example.NuevoProyecto.Subject.Subject;
import com.example.NuevoProyecto.Subject.SubjectService;
import com.example.NuevoProyecto.User.User;
import com.example.NuevoProyecto.User.UserService;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class Control {
    @Autowired
    UserService userService;

    @Autowired
    GradeService gradeService;

    @Autowired
    SubjectService subjectService;



    @GetMapping("/options")
    public String mainPage(Model model, HttpSession session){
        String usern = (String)session.getAttribute("user");

        User user = userService.getUser(usern);

        model.addAttribute("user", userService.getUser(usern));
        if (user.getGrade()!= null) {
            model.addAttribute("grade", userService.getgrade(usern));
            model.addAttribute("yesgrade",true);
        }else{
            model.addAttribute("notgrade",true);
        }
        if (user.getRoles().contains("ADMIN")) {
            model.addAttribute("admin",true);
            model.addAttribute("isLogged",true);
            return "admin";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("admin", true);
        } else {

            model.addAttribute("username", auth.getName());
        }
        return "user";
    }

    @GetMapping("/notregistred")
    public String notregistred(){


        return "not_registred";
    }
}
