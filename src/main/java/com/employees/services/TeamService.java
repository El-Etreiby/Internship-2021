package com.employees.services;

import com.employees.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import com.employees.repositories.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public void saveTeam(Team team){
        teamRepository.save(team);
    }
}
