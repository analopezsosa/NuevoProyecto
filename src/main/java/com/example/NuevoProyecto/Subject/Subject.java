package com.example.NuevoProyecto.Subject;


import com.example.NuevoProyecto.Grade.Grade;
import com.example.NuevoProyecto.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Subject {
    @JsonView(View.Base.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonView(View.Base.class)
    private String name;
    @JsonView(View.Base.class)
    private int subjectNumber;
    @JsonView(View.Base.class)
    private String description;


    public Subject(String name, int subjectNumber, String description){
        this.name = name;
        this.subjectNumber = subjectNumber;
        this.description=description;
        grades = new ArrayList<>();
    }

    @ManyToMany(mappedBy = "subjects")
    private List<Grade> grades = new ArrayList<>();


    public boolean addGrade(Grade grade){

        if (grades.contains(grade)){

            return false;
        }else{

            grades.add(grade);
            return true;
        }



    }

    public void deleteGrade(Grade grade){
        grades.remove(grade);
    }

    public void deleteGradeList(List<Grade> g){
        grades.removeAll(g);
    }
}
