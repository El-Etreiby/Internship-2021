package com.employees;


import com.employees.errorHandling.BadArgumentException;
import com.employees.errorHandling.DepartmentNotFoundException;
import com.employees.models.Department;
import com.employees.repositories.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import lombok.AllArgsConstructor;
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
public class DepartmentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    protected Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDepartmentInsertionTest.xml")
    public void test_insert_department() throws Exception {
        Department newDepartment = new Department();
        newDepartment.setDepartmentName("dep3");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDepartment)))
                .andExpect(status().isOk());

    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_insert_department_unauth() throws Exception {
        Department newDepartment = new Department();
        newDepartment.setDepartmentName("dep3");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/department")
                        .with(httpBasic("b.b", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDepartment)))
                .andExpect(status().isForbidden());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDeleteDepartmentTest.xml")
    public void test_delete_department() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(110)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_delete_non_existing_department() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(119)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DepartmentNotFoundException))
                .andExpect(result -> assertEquals("You're trying to delete a non existing department", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_delete_non_empty_department() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(111)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("this department has members in it and cannot be deleted", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForUpdateDepartmentTest.xml")
    public void test_update_department() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/department/110")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("dep11"))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_update_non_existing_department() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/department/120")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("dep11")))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DepartmentNotFoundException))
                .andExpect(result -> assertEquals("you're trying to update a non existing department", result.getResolvedException().getMessage()));
    }
}
