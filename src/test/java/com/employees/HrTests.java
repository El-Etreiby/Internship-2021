package com.employees;


import com.employees.commands.AddingEmployeeToDepartmentCommand;
import com.employees.commands.AddingEmployeeToTeamCommand;
import com.employees.commands.AddingManagerToEmployeeCommand;
import com.employees.errorHandling.*;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup(value = "/data.xml")
@AutoConfigureMockMvc
public class HrTests {


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
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_salary_history_by_HR() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1/salary")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_salary_by_date_by_HR() throws Exception {
        SalaryId salaryId = new SalaryId();
        salaryId.setYear(2022);
        salaryId.setMonth(5);
        salaryId.setEmployee_id(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/salary")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryId)))
                .andExpect(status().isOk());

    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_salary_by_invalid_date_by_HR() throws Exception {
        SalaryId salaryId = new SalaryId();
        salaryId.setYear(2024);
        salaryId.setMonth(5);
        salaryId.setEmployee_id(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/salary")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SalaryNotFoundException))
                .andExpect(result -> assertEquals("this employee was not paid in the provided date", result.getResolvedException().getMessage()));

    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRaiseTest.xml")
    public void test_raise_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/raise/1000")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_raise_non_existing_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/12/raise/1000")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_invalid_raise_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/raise/-1000")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("a raise must be of a positive value", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_negative_salary() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(-25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("the minimum employee wage is $589", result.getResolvedException().getMessage()));
    }

    @Test
    //   @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForInsertionWithDuplicateNames.xml")
    public void test_employee_creation_and_insertion_with_duplicate_name() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("a");
        newEmployee.setLastName("a");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1006L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String result = encoder.encode("123");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<AccountInformation> acc = accountInformationRepository.findByEmployeeId(5);
        assertThat(encoder.matches("12", acc.get().getPassword())).isFalse();
        assertThat(encoder.matches("123", acc.get().getPassword())).isTrue();
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_negative_national_id() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(-1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("invalid entry for the national ID", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_invalid_first_name() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("1");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's first name should consist of only letters", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_invalid_last_name() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("1");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's last name should consist of only letters", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_invalid_gender() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("o");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2001"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's gender can only be male (represented as 'male') or female (represented as 'female')", result.getResolvedException().getMessage()));
    }

    @Test

    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_with_invalid_dob() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2022"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("Enter a value less than 2021 or more than 1900 for the date of birth", result.getResolvedException().getMessage()));
    }


    //cant use dbuinit in this test due to hashed passwords
    @Test
    // @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForCreationTest.xml")
    public void test_employee_creation_and_insertion() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDegree(Degree.SENIOR);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/1990"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String result = encoder.encode("123");
//       log.info(String.valueOf(encoder.matches("123",result)));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<AccountInformation> acc = accountInformationRepository.findByEmployeeId(5);
        if (acc.isPresent()) {
            log.info("present");
            assertThat(encoder.matches("12", acc.get().getPassword())).isFalse();
            assertThat(encoder.matches("123", acc.get().getPassword())).isTrue();
        }else{
            throw new Exception("employee not created");
        }
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_employee_creation_and_insertion_unauth() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("e");
        newEmployee.setLastName("x");
        newEmployee.setGender("male");
        newEmployee.setGrossSalary(25000.0);
        newEmployee.setNationalId(1001L);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
//        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/password/123")
                        .with(httpBasic("b.b", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDeleteTest.xml")
    public void test_delete_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2")
                        .with(httpBasic("a.a", "123")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_delete_non_existing_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/7")
                        .with(httpBasic("a.a", "123")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("You're trying to delete a non existing employee", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForModifyingRoleTest.xml")
    public void test_modify_role() throws Exception {
        String admin = "ADMIN";
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/2/role")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_role_with_invalid_role() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String role = "HR";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/2/role")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("'EMPLOYEE' and 'ADMIN' are the only valid roles in the system", result.getResolvedException().getMessage()));
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test //fel get est3amel DTOs to return data w fel post e3mel command objects to send data
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_employee_to_non_existing_team() throws Exception {
        AddingEmployeeToTeamCommand command = new AddingEmployeeToTeamCommand();
        command.setEmployeeId(1);
        command.setTeamId(113);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/team/113")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TeamNotFoundException))
                .andExpect(result -> assertEquals("This team does not exist", result.getResolvedException().getMessage()));
    }

    @Test //fel get est3amel DTOs to return data w fel post e3mel command objects to send data
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_non_existing_employee_to_team() throws Exception {
        AddingEmployeeToTeamCommand command = new AddingEmployeeToTeamCommand();
        command.setEmployeeId(12);
        command.setTeamId(112);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/12/team/112")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_employee_to_non_existing_department() throws Exception {
        AddingEmployeeToDepartmentCommand command = new AddingEmployeeToDepartmentCommand();
        command.setEmployeeId(1);
        command.setDepartmentId(110);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/department/110")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DepartmentNotFoundException))
                .andExpect(result -> assertEquals("This department does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_non_existing_employee_to_department() throws Exception {
        AddingEmployeeToDepartmentCommand command = new AddingEmployeeToDepartmentCommand();
        command.setEmployeeId(12);
        command.setDepartmentId(111);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/12/department/111")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_non_existing_manager_to_employee() throws Exception {
        AddingManagerToEmployeeCommand command = new AddingManagerToEmployeeCommand();
        command.setEmployeeId(1);
        command.setManagerId(14);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/1/manager/14")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This manager does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_add_manager_to_non_existing_employee() throws Exception {
        AddingManagerToEmployeeCommand command = new AddingManagerToEmployeeCommand();
        command.setEmployeeId(11);
        command.setManagerId(14);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hr/employee/11/manager/4")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
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
        log.info("modTest: " + modifiedEmployee);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_non_existing_employee() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
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
                        .put("/hr/employee/11")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_first_name() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A#");
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's first name should consist of only letters", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_last_name() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setLastName("$");
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's last name should consist of only letters", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_national_id() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setNationalId(-100L);
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("invalid entry for the national ID", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_dob() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setNationalId(100L);
        modifiedEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/1880"));
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("Enter a value less than 2021 or more than 1900 for the date of birth ", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_graduation_date() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setNationalId(100L);
        modifiedEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("15/1/2030"));
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("Enter a value less than 2021 or more than 1900 for the date of graduation ", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_gender() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setNationalId(100L);
        modifiedEmployee.setGender("ll");
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
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("an employee's gender can only be male (represented as 'male') or female (represented as 'female')", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_modify_employee_invalid_gross_salary() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("A");
        modifiedEmployee.setNationalId(100L);
        modifiedEmployee.setGrossSalary(0.0);
        Optional<Employee> manager = employeeRepository.findById(4);
        Employee managerToAdd = manager.get();
        Optional<Department> department = departmentRepository.findById(111);
        Department departmentToAdd = department.get();
        Optional<Team> team = teamRepository.findById(112);
        Team teamToAdd = team.get();
        modifiedEmployee.setEmployeeTeam(teamToAdd); //should i map fks manually or are they handled by hibernate
        modifiedEmployee.setDepartment(departmentToAdd);
        modifiedEmployee.setManager(managerToAdd);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
                .andExpect(result -> assertEquals("the minimum employee wage is $589", result.getResolvedException().getMessage()));

    }

    @Test
    @Transactional
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForDuplicateNameMOdification.xml")
    public void test_modify_employee_duplicate_name() throws Exception {
        Employee modifiedEmployee = new Employee();
        modifiedEmployee.setFirstName("b");
        modifiedEmployee.setLastName("b");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/hr/employee/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

    }


//    @Test
//    @Transactional
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
//    public void test_modify_employee_non_existing_department() throws Exception {
//        Employee modifiedEmployee = new Employee();
//        modifiedEmployee.setFirstName("A");
//        modifiedEmployee.setNationalId(100L);
//        Optional<Employee> manager = employeeRepository.findById(4);
//        Employee managerToAdd = manager.get();
//        Optional<Department> department = departmentRepository.findById(110);
//        Department departmentToAdd = department.get();
//        Optional<Team> team = teamRepository.findById(112);
//        Team teamToAdd = team.get();
//        modifiedEmployee.setGrossSalary(5000.0);
//        modifiedEmployee.setEmployeeTeam(teamToAdd); //should i map fks manually or are they handled by hibernate
//        modifiedEmployee.setDepartment(departmentToAdd);
//        modifiedEmployee.setManager(managerToAdd);
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/hr/employee/1")
//                        .with(httpBasic("a.a", "123"))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(modifiedEmployee)))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadArgumentException))
//                .andExpect(result -> assertEquals("You must enter a department that exists in the database. If you want to add this employee to this department insert the department in the database first",result.getResolvedException().getMessage()));
//
//    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employee_by_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_non_existing_employee_by_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/6")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("this employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRmovingManagerFromEmployeeTest.xml")
    public void test_remove_manager_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/manager")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_remove_manager_from_non_existing_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/22/manager")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingTeamFromEmployeeTest.xml")
    public void test_remove_team_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_remove_team_from_non_existing_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/24/team")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForRemovingDepartmentFromEmployeeTest.xml")
    public void test_remove_department_from_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/2/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_remove_department_from_non_existing_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/hr/employee/21/department")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/all")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/1/salary")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_non_existing_employee_salary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employee/11/salary")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("this employee does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_under_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesUnderManager/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_employees_under_non_existing_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/employeesUnderManager/12")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This manager does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees_under_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/allEmployeesUnderManager/1")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_all_employees_under_non_existing_manager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hr/allEmployeesUnderManager/11")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("This employee does not exist", result.getResolvedException().getMessage()));
    }
}
