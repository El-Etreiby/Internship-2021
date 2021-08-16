package com.employees.models;


import com.employees.services.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Test
    public void test_add_new_team(){
        Team newTeam = new Team();
        newTeam.setTeamName("Team 2");
        String response = teamService.addNewTeam(newTeam);
        assertEquals("Team added successfully!",response);
    }

    @Test
    public void test_remove_team() throws Exception {
        String response = teamService.removeTeam(5);
        assertEquals("Team removed successfully!",response);
    }
}
