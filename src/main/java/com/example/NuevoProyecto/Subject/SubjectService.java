package com.example.NuevoProyecto.Subject;


import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public List<Subject> subjectList(){
        return subjectRepository.findAll();
    }


    public boolean newSubject(Subject subject) {
        if(!subjectRepository.existsById(subject.getId())) {
            subjectRepository.save(subject);
            return true;
        } else {
            return false;
        }
    }

    public Subject updateSubject(long id, Subject s){
        s.setId(id);
        s.setDescription(Sanitizers.FORMATTING.sanitize(s.getDescription()));


        return subjectRepository.save(s);
    }

    public boolean removeSubject(Long id){
        subjectRepository.deleteById(id);
        return true;
    }
    public List<Subject> getSubjectList(){
        return subjectRepository.findAll();
    }
    public void saveSubject(Subject s){
        subjectRepository.save(s);
    }

    public Subject getSubject(long id){
        Optional<Subject> subject = subjectRepository.findById(id);
        if(subject.isEmpty()){
            return null;
        }
        return subject.get();
    }

    public Subject deleteSubject(long id){

        Subject s= subjectRepository.getById(id);

        subjectRepository.deleteById(id);
        return s;
    }

    public void addSubject(Subject subjectToAdd) {
        subjectToAdd.setDescription(Sanitizers.FORMATTING.sanitize(subjectToAdd.getDescription()));

        subjectRepository.save(subjectToAdd);
    }
}
