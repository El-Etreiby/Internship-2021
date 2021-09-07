package com.employees.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

import javax.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    private String teamName;

//    @JsonManagedReference (value = "employee's team")
//    @OneToMany(mappedBy = "employeeTeam",
//            cascade = CascadeType.PERSIST,
//            fetch = FetchType.EAGER) //mappedBy = "fk attribute in other table name"
//    private List<Employee> teamMembers;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

//    public List<Employee> getTeamMembers() {
//        return teamMembers;
//    }
//
//    public void setTeamMembers(List<Employee> teamMembers) {
//        this.teamMembers = teamMembers;
//    }
}
