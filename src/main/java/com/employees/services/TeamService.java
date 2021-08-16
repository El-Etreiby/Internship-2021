package com.employees.services;

import com.employees.models.Department;
import com.employees.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import com.employees.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public void saveTeam(Team team){
        teamRepository.save(team);
    }

    public void removeTeam(Integer teamToBeRemoved) throws Exception {
        Optional<Team> toBeRemoved = teamRepository.findById(teamToBeRemoved);
        if (toBeRemoved.isPresent())
            teamRepository.deleteById(teamToBeRemoved);
        else
            throw new Exception("You're trying to delete a non existing team");
    }
}
