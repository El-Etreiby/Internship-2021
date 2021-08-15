package com.employees.controllers;

import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.services.EmployeeService;
import com.employees.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/")
@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping(path = "/addNewTeam") // Map ONLY POST Requests
    @ResponseBody
    public String addNewTeam(@RequestBody Team team)
    // @RequestParam means it is a parameter from the GET or POST request
    {
        //WHAT ABOUT FKS (manager, managed team, team....) ?????
        teamService.saveTeam(team);
        return "Team Saved!";
    }



}
