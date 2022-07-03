package com.example.NuevoProyecto.Subject;

import com.example.NuevoProyecto.Grade.Grade;
import com.example.NuevoProyecto.Grade.GradeService;
import com.example.NuevoProyecto.User.User;
import com.example.NuevoProyecto.User.UserService;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public String showSubjects(Model model, HttpSession session) {

        String user = (String)session.getAttribute("user");
        loginDisplay(model);
        User aux = userService.getUser(user);
        if(aux.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }
        model.addAttribute("subjects", subjectService.getSubjectList());

        return "viewsubjects";
    }


    @GetMapping("/create")
    public String showSubject(Model model) {

        return "createsubject";
    }


    @PostMapping("/create")
    public String createSubject(@RequestParam String name, @RequestParam int subjectNumber, @RequestParam String description, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        Subject newsubject = new Subject(name, subjectNumber, Sanitizers.FORMATTING.sanitize(description));
        subjectService.saveSubject(newsubject);
        model.addAttribute("subject",newsubject);

        return "viewsubject";
    }

    /*
    @GetMapping("/edit")
    public String showEdit(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        return "editsubject";
    }*/
    @PostMapping("/edit")
    public String editSubject(@RequestParam long id, @RequestParam String name, @RequestParam int subjectNumber, @RequestParam String description, Model model, HttpSession session){
        String infoname = ( String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }
        Subject editThisSubject = subjectService.getSubject(id);
        if(editThisSubject != null){
            editThisSubject.setName(name);
            editThisSubject.setSubjectNumber(subjectNumber);
            editThisSubject.setDescription(Sanitizers.FORMATTING.sanitize(description));
            subjectService.addSubject(editThisSubject);
            model.addAttribute("subjects",subjectService.getSubjectList());
            return "viewsubjects";
        }
/*

        return "editedsubject";

 */
        return "error";
    }
/*
    @GetMapping("/{id}/addtograde")
    public String showAddSubject(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        return "addsubjecttograde";
    }
*/

    @PostMapping("/addtograde")
    public String addingSubjectToGrade(@RequestParam long idS, @RequestParam long idG, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        Subject subjectToAdd = subjectService.getSubject(idS);
        if(subjectToAdd!=null){
            Grade g = gradeService.getGrade(idG);

            g.addSubject(subjectToAdd);
            subjectService.addSubject(subjectToAdd);
            gradeService.addGrade(g);
            subjectToAdd.getGrades().add(g);

            model.addAttribute("grade",gradeService.getGrade(idG));
            return "viewsubjectsbygrade";
        }
        return "error";
    }

/*
    @GetMapping("/removesubject.html")
    public String showRemove(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        return "removesubject";
    }
*/
    @PostMapping("/remove")
    public String removeSubject( @RequestParam Long id, Model model, HttpSession session){
        String infoname = ( String) session.getAttribute("user");
        User u = userService.getUser(infoname);
        if(u.getRoles().contains("ADMIN")){
            model.addAttribute("admin",true);
        }
        Subject subject = subjectService.getSubject(id);

        if(subject != null){

            deleteGrades(id);
            subjectService.deleteSubject(id);
            model.addAttribute("subjects",subjectService.getSubjectList());

            return "viewsubjects";
        }
        return "error";



    }
/*
    @GetMapping("/removesubjectfromgrade")
    public String showRemoveFromGrade(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        return "removesubjectfromgrade";
    }

 */
    @PostMapping("/removefromgrade")
    public String remove(@RequestParam Long idS, @RequestParam Long idG, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("403", true);
            return "error";
        }
        loginDisplay(model);
        Subject subjectToRemove = subjectService.getSubject(idS);
        Grade g = gradeService.getGrade(idG);
        if (g != null){
            g.deleteSubject(subjectToRemove);
            subjectToRemove.getGrades().remove(g);

            gradeService.addGrade(g);
            model.addAttribute("grade",gradeService.getGrade(idG));
            return "viewsubjectsbygrade";
        }
        return "error";
    }



    public void deleteGrades(long id){
        Subject subject = subjectService.getSubject(id);
        List<Grade> gradesToDeleteFromSubject=subject.getGrades();
        int x=gradesToDeleteFromSubject.size();
        for (int i=0;i<x;i++) {
            gradesToDeleteFromSubject.get(i).deleteSubject(subject);
        }
        subject.deleteGradeList(gradesToDeleteFromSubject);
        subjectService.addSubject(subject);

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
