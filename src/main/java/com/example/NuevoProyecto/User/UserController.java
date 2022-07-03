package com.example.NuevoProyecto.User;

import com.example.NuevoProyecto.Grade.Grade;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.security.core.Authentication;
import com.example.NuevoProyecto.Grade.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private PasswordEncoder passwordEncoder;



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
        loginDisplay(model);
        return "login";
    }


    @GetMapping("/login")
    public String showLogin() {
        System.out.println("Primero");
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,@RequestParam String password, Model model){
        User user=userService.getUser(username);
        System.out.println("Segundo");
        if(user==null) {

            return "signup";
        }else if(passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Tercero");
            model.addAttribute("user", userService.getUser(username));
            if (user.getGrade()!= null) {
                model.addAttribute("grade", userService.getgrade(username));
                model.addAttribute("yesgrade",true);
            }else{
                model.addAttribute("notgrade",true);
            }
            if (user.getRoles().contains("ADMIN")) {
                return "admin";
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                model.addAttribute("admin", true);
            } else {
                System.out.println("Cuarto");
                model.addAttribute("username", auth.getName());
            }
            return "user";
        }
        return "login";
    }


    @PostMapping("/joingradeU")
    public String joinGradeU(@RequestParam long id, Model model){
        loginDisplay(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userToJoin = auth.getName();
        System.out.println(userToJoin);
        //User userToJoin = userService.userRepository.getById(infoname);
        Grade gradeToJoin=gradeService.getGrade(id);
        if (userService.getUser(userToJoin).getGrade()==null) {
            System.out.println("\nno tiene grade");
            userService.getUser(userToJoin).setGrade(gradeToJoin);
            gradeToJoin.addUser(userService.getUser(userToJoin));
            userService.addUser(userService.getUser(userToJoin));
            gradeService.addGrade(gradeToJoin);
            //model.addAttribute("user",auth.getName());
            model.addAttribute("user",userService.getUser(userToJoin));
            model.addAttribute("yesgrade",true);
            model.addAttribute("grade",gradeToJoin);
            return "user";
        }else{
            return "error";
        }
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
