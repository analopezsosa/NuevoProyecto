package com.example.NuevoProyecto.Grade;


import com.example.NuevoProyecto.Subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public void saveGrade(Grade grade){
        gradeRepository.save(grade);
    }

    public List<Grade> gradeList(){
        return gradeRepository.findAll();
    }

    public Grade getGrade(long id){
        return gradeRepository.getById(id);
    }


    public Grade deleteGrade(long id){

        Grade grade= gradeRepository.getById(id);

        gradeRepository.deleteById(id);
        return grade;
    }

    public void addGrade(Grade g) {
        gradeRepository.save(g);
    }

    public Grade updateGrade(long id,Grade g){

        g.setId(id);
        return gradeRepository.save(g);


    }
}
