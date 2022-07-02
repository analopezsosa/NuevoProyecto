package com.example.NuevoProyecto.User;


import com.example.NuevoProyecto.Grade.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        session.setAttribute("user", username);

        User user = userService.getUser(username);
        if (user == null) {
            model.addAttribute("notRegistered", true);
            return "signup";
        } else if (passwordEncoder.matches(password, user.getPassword())) {

            model.addAttribute("user", username);

            if (user.getGrade() != null) {
                model.addAttribute("userGrade", userService.getUser(username).getGrade());
            }
            if (user.getRoles().contains("ADMIN")) {
                model.addAttribute("admin", true);
                return "admin";
            }

            return "user";
        } else {

            return "login";
        }


    }
}
