package com.example.NuevoProyecto.Grade;


import com.example.NuevoProyecto.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class GradeRESTController {
    @Autowired
    private GradeService gradeService;


    @GetMapping("/grades")
    @JsonView(View.Base.class)
    public ResponseEntity<Collection> gradeList(){return new ResponseEntity<>( gradeService.gradeList(),HttpStatus.OK);}

    @GetMapping("/grades/{id}")
    @JsonView(View.Base.class)
    public ResponseEntity<Grade> viewGrade(@PathVariable long id){
        Grade grade=gradeService.getGrade(id);
        if(grade!=null){
            return new ResponseEntity<>(grade,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/grades")
    @JsonView(View.Base.class)
    public ResponseEntity<Grade> addGrade(@RequestBody Grade grade){
        gradeService.addGrade(grade);
        if(grade != null){
            return new ResponseEntity<>(grade, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @PutMapping("/grades/{id}")
    @JsonView(View.Base.class)
    public ResponseEntity updateGrade(@PathVariable long id, @RequestBody Grade grade){
        Grade gradeT = gradeService.updateGrade(id,grade);

        if(gradeT != null){

            return new ResponseEntity<>(gradeT, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/grades/{id}")
    @JsonView(View.Base.class)
    public ResponseEntity<Grade> deleteGrade(@PathVariable long id){
        Grade gradeT = gradeService.deleteGrade(id);
        if (gradeT != null){
            return new ResponseEntity<>( HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

