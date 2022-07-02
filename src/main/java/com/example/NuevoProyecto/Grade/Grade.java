package com.example.NuevoProyecto.Grade;

import com.example.NuevoProyecto.Subject.Subject;
import com.example.NuevoProyecto.User.User;
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
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonView(View.Base.class)
    private String name;
    @JsonView(View.Base.class)
    private int gradeNumber;
    @JsonView(View.Base.class)
    private String teacher;

    @OneToMany()
    private List<User> userList = new ArrayList<>();
    @JsonView(View.Base.class)
    @ManyToMany
    private List<Subject> subjects;


    public Grade(String name, int gradeNumber, String teacher) {
        this.name = name;
        this.gradeNumber = gradeNumber;
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public int getGradeNumber(){
        return this.gradeNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return " Curso: "+name + " Número: "+ gradeNumber + ". Con ID: " + id;
    }

    public long getId(){
        return this.id;
    }

    public List<User> getUserList(){
        return this.userList;
    }
    public void setUsers(List<User> users){
        this.userList = users;
    }

    public void setGradeNumber(int gradeNumber) {
        this.gradeNumber = gradeNumber;
    }


    public void addUser(User user) {

        if (!userList.contains(user)) {
            userList.add(user);

        }
    }

    public boolean addSubject(Subject subject){
        if (subjects.contains(subject)) {

            return false;
        }else{
            subjects.add(subject);
            return true;
        }

    }

    public void deleteSubject(Subject subject){subjects.remove(subject);}
    public List<Subject> getSubjects(){
        return this.subjects;
    }

    public void updateGrade(Grade g) {
        this.subjects = g.subjects;
    }
    public void deleteUser(User user){
        userList.remove(user);
    }

    public void deleteUserList(List<User> u){
        userList.removeAll(u);
    }
}
