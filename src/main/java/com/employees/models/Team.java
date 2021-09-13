package com.employees.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @Column(unique = true, nullable = false)
    private String teamName;

    public String toString(){
        return "team ID: " + this.teamId + "\n"
                + "team name: " + this.teamName + "\n";
    }

}
