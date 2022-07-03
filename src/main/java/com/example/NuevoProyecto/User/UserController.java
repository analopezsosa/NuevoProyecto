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
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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


    @GetMapping("/")
    public String showusers(Model model) {
        loginDisplay(model);
        model.addAttribute("users",userService.getUsers());
        return "viewusers";
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
        loginDisplay(model);
        return "login";
    }


    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,@RequestParam String password, Model model, HttpSession session){

        User user=userService.getUser(username);
        session.setAttribute("user",username);
        if(user==null) {

            return "signup";
        }else if(passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("user", userService.getUser(username));
            if (user.getGrade()!= null) {
                model.addAttribute("grade", userService.getgrade(username));
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
        return "login";
    }


    @PostMapping("/joingradeU")
    public String joinGradeU(@RequestParam long id, Model model,HttpSession session){

        String userToJoin = (String)session.getAttribute("user");
        System.out.println(userToJoin);

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

    @PostMapping("/joingrade")
    public String joinGrade(@RequestParam String username,@RequestParam long id, Model model,HttpSession session){
        String infoname = (String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        System.out.println("hasta aqui?");
        if(u.getRoles().contains("ADMIN")){
            System.out.println("sabe que eres adminnnnn");
            User userToJoin = userService.getUser(username);
            Grade gradeToJoin=gradeService.getGrade(id);

            if (userToJoin.getGrade()==null) {

                userToJoin.setGrade(gradeToJoin);
                gradeToJoin.addUser(userToJoin);
                userService.addUser(userToJoin);
                gradeService.addGrade(gradeToJoin);

                model.addAttribute("admin",true);
                model.addAttribute("user",u);
                return "admin";

            }else{
                return "error";
            }
        }
        return "error";

    }

    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping("/filter")
    public String filterUsers(Model model, @RequestParam(required = false, name= "username") String username, @RequestParam(required = false, name = "lastName") String lastName){
        if (username!="" && lastName!=""){
            TypedQuery<User> q1 = entityManager.createQuery("SELECT u FROM User u where u.user = :username  and u.lastName= :lastName",User.class);
            q1.setParameter("lastName",lastName).setParameter("username",username);
            model.addAttribute("users", q1.getResultList());
        }
        else if (username!=""){
            TypedQuery<User> q2 = entityManager.createQuery("SELECT u FROM User u WHERE u.user = :username",User.class);
            model.addAttribute("users", q2.setParameter("username",username).getResultList());
        }
        else if (lastName!=""){
            TypedQuery<User> q2 = entityManager.createQuery("SELECT u FROM User u WHERE u.lastName = :lastName",User.class);
            model.addAttribute("users", q2.setParameter("lastName",lastName).getResultList());
        }
        else{
            model.addAttribute("users",userService.getUsers());
        }

        return "viewusers";
    }


    @GetMapping("/{user}")
    public String viewuser(Model model,@PathVariable String user, HttpSession session){
        String infoname = (String) session.getAttribute("user");
        User u = userService.getUser(user);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }


        if (u.getGrade()!= null) {
            System.out.println("entra aqui?");
            model.addAttribute("grade", userService.getgrade(u.getUser()));
            model.addAttribute("yesgrade",true);
        }else{
            System.out.println("o aaqui?");
            model.addAttribute("notgrade",true);
        }


        if(u != null){
            model.addAttribute("user",userService.getUser(user));
            return "viewuser";
        }
        return "error";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String username, HttpSession session, Model model){
        String infoname = ( String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }

        User user = userService.getUser(username);
        if(user != null&&user.getGrade()!=null){
            Grade grade=user.getGrade();
            user.deleteGrade(grade);
            grade.deleteUser(user);
            gradeService.addGrade(grade);
            userService.removeUser(username);
        }else if(user!=null){
            userService.removeUser(username);

        }
        model.addAttribute("users",userService.getUsers());
        return "viewusers";

    }


    @PostMapping("/removefromgrade")
    public String removefromgrade(@RequestParam String name,@RequestParam long id,  HttpSession session, Model model){
        String infoname = ( String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }

        User user=userService.getUser(name);
        Grade grade=gradeService.getGrade(id);
        user.deleteGrade(grade);
        grade.deleteUser(user);
        gradeService.addGrade(grade);
        model.addAttribute("users",userService.getUsers());
        return "viewusers";

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
