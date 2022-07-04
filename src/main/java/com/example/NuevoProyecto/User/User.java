package com.example.NuevoProyecto.User;


import com.example.NuevoProyecto.Grade.Grade;
import com.example.NuevoProyecto.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class User {
    @Id
    @JsonView(View.Base.class)
    private String user;
    @JsonView(View.Base.class)
    private String lastName;

    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;


    @JsonView(View.Base.class)
    @ManyToOne
    @JoinColumn(name="userGrade")
    private Grade grade;


    public User(String user, String password, String lastName, List<String> roles) {
        this.user = user;
        this.password = password;
        this.lastName = lastName;
        this.roles = roles;
    }
    public User(String user, String password, String lastName){
        this.user = user;
        this.password = password;
        this.lastName = lastName;
        roles = new ArrayList<>();
        roles.add("USER");
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void removeRole(String role) {
        roles.remove(role);
    }



    public String toString() {
        if(grade != null){
            return "Usuario: "+user +", Apellido: "+ lastName+" ,Curso: "+grade.getName();
        }else{
            return "Usuario: "+user +", Apellido: "+ lastName;
        }

    }

    public boolean checkPass(String password) {
        return this.password.equals(password);
    }

    public Grade getGrade() {
        return grade;
    }
    public void deleteGrade(Grade g){
        if(this.grade == g){
            this.grade = null;
        }
    }

    public String getUser(String user) {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setGrade(Grade grade) {
        this.grade = grade;
    }





}

