package com.employees.services;

import com.employees.errorHandling.BadArgumentException;
import com.employees.errorHandling.InternalException;
import com.employees.errorHandling.TeamNotFoundException;
import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.employees.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String addNewTeam(Team team){
        Iterable<Team> teams = teamRepository.findAll();
        Iterator<Team> allTeams = teams.iterator();
        while(allTeams.hasNext()){
            if(allTeams.next().getTeamName() == team.getTeamName()){
                throw new InternalException("Cannot create teams with duplicate names");
            }
        }
         teamRepository.save(team);
        return "Team added successfully!";
    }

    public String removeTeam(Integer teamToBeRemoved) throws Exception {
        Optional<Team> toBeRemoved = teamRepository.findById(teamToBeRemoved);
        if (toBeRemoved.isPresent()) {
            Iterable<Employee> employees = employeeRepository.findAll();
            Iterator<Employee> allEmployees = employees.iterator();
            while(allEmployees.hasNext()){
                Employee temp = allEmployees.next();
                System.out.println("next employee: " + temp);
                System.out.println(temp.getEmployeeTeam()==null);
                if(temp.getEmployeeTeam()!=null && temp.getEmployeeTeam().getTeamId()==teamToBeRemoved){
                    throw new BadArgumentException("this team has members in it and cannot be deleted");
                }
            }
            teamRepository.deleteByID(teamToBeRemoved);
            return "Team removed successfully!";
        }
        else
            throw new TeamNotFoundException("You're trying to delete a non existing team");
    }

    public void updateTeam(String newTeamName, Integer teamId) {
        Optional<Team> toBeUpdated = teamRepository.findById(teamId);
        if (!toBeUpdated.isPresent()) {
            throw new TeamNotFoundException("you're trying to update a non existing team");
        }
        Team team = toBeUpdated.get();
        team.setTeamName(newTeamName);
        teamRepository.save(team);
    }
}
