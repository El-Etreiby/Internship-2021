package com.employees.controllers;

import com.employees.models.Team;
import com.employees.services.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/team")
@RestController
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;

    @PostMapping(path = "") // Map ONLY POST Requests {id}
    @ResponseBody
    public String addNewTeam(@RequestBody Team team)
    // @RequestParam means it is a parameter from the GET or POST request
    {
        teamService.addNewTeam(team);
        return "Team Saved!";
    }
    @DeleteMapping(path = "") // Map ONLY POST Requests
    @ResponseBody
    public String removeTeam(@RequestBody Integer teamToBeRemoved) throws Exception {
        teamService.removeTeam(teamToBeRemoved);
        return "Team Removed!";
    }

    @PutMapping(path = "/{teamId}") // Map ONLY POST Requests
    @ResponseBody
    public String updateTeam(@RequestBody String newName,@PathVariable String teamId) {
        teamService.updateTeam(newName,Integer.parseInt(teamId));
        return "Team Updated!";
    }


}
