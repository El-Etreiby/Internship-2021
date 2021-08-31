package com.employees;

import com.employees.commands.AddingEmployeeToDepartmentCommand;
import com.employees.commands.AddingEmployeeToTeamCommand;
import com.employees.commands.AddingManagerToEmployeeCommand;
import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.models.Team;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup(value = "/data.xml")
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
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForCreationTest.xml")
    public void test_employee_creation_and_insertion() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
//        newEmployee.setEmployeeId(5);
        newEmployee.setGender('M');
        newEmployee.setGrossSalary(25000.0);
//        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDeleteTest.xml")
    public void test_delete_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test //fel get est3amel DTOs to return data w fel post e3mel command objects to send data
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForAddingTeamTest.xml")
    public void test_add_employee_to_team() throws Exception {
        AddingEmployeeToTeamCommand command = new AddingEmployeeToTeamCommand();
        command.setEmployeeId(1);
        command.setTeamId(112);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/team/112")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForAddingDepartmentTest.xml")
    public void test_add_employee_to_department() throws Exception {
        AddingEmployeeToDepartmentCommand command = new AddingEmployeeToDepartmentCommand();
        command.setEmployeeId(1);
        command.setDepartmentId(111);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/department/111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForAddingManagerTest.xml")
    public void test_add_manager_to_employee() throws Exception {
        AddingManagerToEmployeeCommand command = new AddingManagerToEmployeeCommand();
        command.setEmployeeId(1);
        command.setManagerId(4);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/manager/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    //{
    //    "manager": {"employeeId" : "15"}
    //}


    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForModifyingTest.xml")
    public void test_modify_employee() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
//        modifiedEmployee.setGender('F');
        Optional<Employee> manager = employeeRepository.findById(4);
        Employee managerToAdd = manager.get();
        Optional<Department> department = departmentRepository.findById(111);
        Department departmentToAdd = department.get();
        Optional<Team> team = teamRepository.findById(112);
        Team teamToAdd = team.get();
        modifiedEmployee.setGrossSalary(5000.0);
        modifiedEmployee.setEmployeeTeam(teamToAdd);
        modifiedEmployee.setDepartment(departmentToAdd);
        modifiedEmployee.setManager(managerToAdd);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employee_by_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRmovingManagerFromEmployeeTest.xml")
    public void test_remove_manager_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/manager")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingTeamFromEmployeeTest.xml")
    public void test_remove_team_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingDepartmentFromEmployeeTest.xml")
    public void test_remove_department_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1/salary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_under_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesUnderManager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees_under_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/allEmployeesUnderManager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_get_salary_history() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRaiseTest.xml")
    public void test_raise_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/raise/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForVacationRequest.xml")
    public void test_employee_vacation_request() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/vacation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_in_team() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesInTeam/team/112")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
