package com.example.NuevoProyecto.Grade;


import com.example.NuevoProyecto.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserService userService;



}
