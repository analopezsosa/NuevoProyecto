package com.example.NuevoProyecto.User;

import org.springframework.security.core.Authentication;
import com.example.NuevoProyecto.Grade.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    GradeService gradeService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public  String index(){
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String lastName, Model model) {

        if (userService.getUser(username) != null) {
            model.addAttribute("error", true);
            return "signup";
        }

        User newUser = new User(username, passwordEncoder.encode(password), lastName);
        userService.addUser(newUser);
        return "login";
    }


    @GetMapping("/login")
    public String showLogin() {

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String name,@RequestParam String password, Model model){
        User user=userService.getUser(name);
        if(user==null) {
            return "signup";
        }else if(passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("user", userService.getUser(name));
            if (user.getGrade()!= null) {
                model.addAttribute("grade", userService.getgrade(name));
            }
            if (user.getRoles().contains("ADMIN")) {
                return "admin";
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("admin", true);
            } else {
                model.addAttribute("user", auth.getName());
            }
            return "user";
        } else {
            return "login";
        }
    }
}
