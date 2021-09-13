package com.employees;


import com.employees.errorHandling.BadArgumentException;
import com.employees.errorHandling.TeamNotFoundException;
import com.employees.models.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup(value = "/data.xml")
@AutoConfigureMockMvc
public class TeamTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForTeamInsertionTest.xml")
    public void test_insert_team() throws Exception {
        Team newTeam = new Team();
        newTeam.setTeamName("team3");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeam)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_insert_team_unauth() throws Exception {
        Team newTeam = new Team();
        newTeam.setTeamName("team 2");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/team")
                        .with(httpBasic("b.b", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeam)))
                .andExpect(status().isForbidden());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDeleteTeamTest.xml")
    public void test_delete_team() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(113)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_delete_non_empty_team() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(112)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("this team has members in it and cannot be deleted", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_delete_non_existing_team() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(115)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TeamNotFoundException))
                .andExpect(result -> assertEquals("You're trying to delete a non existing team", result.getResolvedException().getMessage()));
    }
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForUpdateTeamTest.xml")
    public void test_update_team() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/team/112")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("team11"))
                .andExpect(status().isOk());
              }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_update_non_existing_team() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/team/115")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("team11")))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TeamNotFoundException))
                .andExpect(result -> assertEquals("you're trying to update a non existing team", result.getResolvedException().getMessage()));
    }

}
