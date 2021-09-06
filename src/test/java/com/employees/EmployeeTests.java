package com.employees;

import com.employees.commands.AddingEmployeeToDepartmentCommand;
import com.employees.commands.AddingEmployeeToTeamCommand;
import com.employees.commands.AddingManagerToEmployeeCommand;
import com.employees.models.*;
import com.employees.repositories.AccountInformationRepository;
import com.employees.repositories.DepartmentRepository;
import com.employees.repositories.EmployeeRepository;
import com.employees.repositories.TeamRepository;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup(value = "/data.xml")
//@ContextConfiguration
@AutoConfigureMockMvc
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private AccountInformationRepository accountInformationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    protected Flyway flyway;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }


    @Test
    //@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForCreationTest.xml")
    public void test_employee_creation_and_insertion() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender('M');
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/1990"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<Employee> employee = employeeRepository.findById(5);
        if (!employee.isPresent()) {
            throw new Exception("emp not found");
        }else{
            log.info("emp found");
        }
        Iterable<AccountInformation> account = accountInformationRepository.findAll();
        Iterator iterator = account.iterator();
        while (iterator.hasNext()) {
            AccountInformation acc = (AccountInformation) iterator.next();
            if (acc.getEmployee().getEmployeeId() == 5) {
             //   if (!acc.getUsername().equals("e.x") || !acc.getPassword().equals("{bcrypt}$2a$10$6ibpKBviRZ2BUaCc.NuWxeN1eYfiliNn.98qF2S72IMc87stUr1ci")) {
                if (!acc.getUsername().equals("e.x")){
                    log.info("user: " + acc.getUsername() + "\n");
                    log.info("pw: " + acc.getPassword());
                    throw new Exception("invalid credentials");
                } else {
                    log.info("success!!!!");
                }
            }
        }


    }

//    @Test
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
//    public void test_employee_creation_and_insertion_unauth() throws Exception {
//        Employee newEmployee = new Employee();
//        newEmployee.setFirstName("e");
//        newEmployee.setLastName("x");
//        newEmployee.setGender('M');
//        newEmployee.setGrossSalary(25000.0);
//        newEmployee.setNationalId(1001L);
//        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
////        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/hr/employee/password/123")
//                        .with(httpBasic("user2", "123"))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newEmployee)))
//                .andDo(print())
//                .andExpect(status().isOk());
//        Optional<Employee> employee = employeeRepository.findById(5);
//        if (!employee.isPresent()) {
//            throw new Exception("emp not found");
//        }else{
//            log.info("emp found");
//        }
//        Iterable<AccountInformation> account = accountInformationRepository.findAll();
//        Iterator iterator = account.iterator();
//        while (iterator.hasNext()) {
//            AccountInformation acc = (AccountInformation) iterator.next();
//            if (acc.getEmployee().getEmployeeId() == 5) {
//                //   if (!acc.getUsername().equals("e.x") || !acc.getPassword().equals("{bcrypt}$2a$10$6ibpKBviRZ2BUaCc.NuWxeN1eYfiliNn.98qF2S72IMc87stUr1ci")) {
//                if (!acc.getUsername().equals("e.x")){
//                    log.info("user: " + acc.getUsername() + "\n");
//                    log.info("pw: " + acc.getPassword());
//                    throw new Exception("invalid credentials");
//                } else {
//                    log.info("success!!!!");
//                }
//            }
//        }
//
//
//    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDeleteTest.xml")
    public void test_delete_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2")
                .with(httpBasic("user1", "123")))
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
                        .with(httpBasic("user1", "123"))
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
                        .with(httpBasic("user1", "123"))
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
                        .with(httpBasic("user1", "123"))
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
        modifiedEmployee.setEmployeeTeam(teamToAdd); //should i map fks manually or are they handled by hibernate
        modifiedEmployee.setDepartment(departmentToAdd);
        modifiedEmployee.setManager(managerToAdd);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/1")
                        .with(httpBasic("user1", "123"))
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
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRmovingManagerFromEmployeeTest.xml")
    public void test_remove_manager_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/manager")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingTeamFromEmployeeTest.xml")
    public void test_remove_team_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/team")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingDepartmentFromEmployeeTest.xml")
    public void test_remove_department_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/department")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/all")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1/salary")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_under_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesUnderManager/1")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees_under_manager() throws Exception {
        Iterable<Employee> employees = employeeRepository.findAll();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/allEmployeesUnderManager/1")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_get_salary_history() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryHistory")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForUsernameUpdateTest.xml")
//    public voidf test_employee_update_username() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/employee/username")
//                        .with(httpBasic("user1", "123"))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString("USER1")))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRaiseTest.xml")
    public void test_raise_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/raise/1000")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    public void test_issue_salary()
//            throws InterruptedException {
//        Thread.sleep(100L);
//
//        assertThat(counter.getInvocationCount()).isGreaterThan(0);
//    }

//    @Test
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForIssueSalaryTest.xml")
//    public void test_issue_salary() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Optional<Employee> employee = employeeRepository.findById(2);
//        Salary newSalary = new Salary();
//        newSalary.setEmployee(employee.get());
//        SalaryId salaryId = new SalaryId();
//        salaryId.setEmployeeId(2);
//        salaryId.setMonth(4);
//        salaryId.setYear(2021);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/hr/employee/salary")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newSalary)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForVacationRequest.xml")
    public void test_employee_vacation_request() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/vacation")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_in_team() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesInTeam/team/112")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_days_off_by_date() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/daysOff/month/5/year/2022")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_salary_history() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryHistory")
                        .with(httpBasic("user1", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
