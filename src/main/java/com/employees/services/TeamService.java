package com.employees.services;

import com.employees.errorHandling.InternalException;
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
                if(allEmployees.next().getEmployeeTeam()!=null && allEmployees.next().getEmployeeTeam().getTeamId()==teamToBeRemoved){
                    throw new InternalException("this team has members in it and cannot be deleted");
                }
            }
            teamRepository.deleteById(teamToBeRemoved);
            return "Team removed successfully!";
        }
        else
            throw new InternalException("You're trying to delete a non existing team");
    }
}
