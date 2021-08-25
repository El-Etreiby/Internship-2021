package com.employees;

import com.employees.commands.AddingEmployeeToDepartmentCommand;
import com.employees.commands.AddingEmployeeToTeamCommand;
import com.employees.commands.AddingManagerToEmployeeCommand;
import com.employees.models.Employee;
import com.employees.repositories.DepartmentRepository;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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

import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest(EmployeeController.class)
//@SpringBootTest


@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    protected Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }


    @Test
    @DatabaseSetup(value = "/data.xml")
    @ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT, value= "/expectedDataForCreationTest.xml")
    public void test_employee_creation_and_insertion() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeName("aaa");
        newEmployee.setEmployeeId(1);
        newEmployee.setGender('M');
        newEmployee.setGrossSalary(5500.0);
//        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk());

    }
    @Test
    @DatabaseSetup(value = "/expectedDataForCreationTest.xml")
    @ExpectedDatabase(assertionMode= DatabaseAssertionMode.NON_STRICT, value= "/data.xml")
    public void test_delete_employee() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test //fel get est3amel DTOs to return data w fel post e3mel command objects to send data

    public void test_add_employee_to_team() throws Exception {
        AddingEmployeeToTeamCommand command = new AddingEmployeeToTeamCommand();
        command.setEmployeeId(1);
        command.setTeamId(10);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/17/team/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_add_employee_to_department() throws Exception {
        AddingEmployeeToDepartmentCommand command = new AddingEmployeeToDepartmentCommand();
        command.setEmployeeId(14);
        command.setDepartmentId(9);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/14/department/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_add_manager_to_employee() throws Exception {
        AddingManagerToEmployeeCommand command = new AddingManagerToEmployeeCommand();
        command.setEmployeeId(1);
        command.setManagerId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/1/manager/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    //{
    //    "manager": {"employeeId" : "15"}
    //}


    @Test
    public void test_modify_employee() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setEmployeeName("SsSsS");
//        modifiedEmployee.setGender('F');
        modifiedEmployee.setGrossSalary(1313.13);
//        Employee manager = new Employee();
//        manager.setEmployeeName("MAN");
//        employeeRepository.save(manager);
//        modifiedEmployee.setManager(manager);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andExpect(status().isOk());

    }
    @Test
    public void test_get_employee_by_id() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/14")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_remove_manager_from_employee() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/14/manager")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_remove_team_from_employee() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/14/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void test_remove_department_from_employee() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/14/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_all_employees() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/15/salary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_employees_under_manager() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/manager/20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_all_employees_under_manager() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/manager/14/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_employees_in_team() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/team/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
//at2aked ezay en el entities fe3lan related f java?????
}
