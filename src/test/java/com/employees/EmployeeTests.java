package com.employees;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AccountInformationRepository accountInformationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private Flyway flyway;


    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
   // @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_change_employee_password() throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/password")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("234"))
                .andExpect(status().isOk());

        Optional<AccountInformation> account = accountInformationRepository.findByEmployeeId(1);
        if (account.isPresent()) {
            AccountInformation acc = account.get();
            log.info("pw: " + acc.getPassword());
            assertThat(encoder.matches("123", acc.getPassword())).isFalse();
            assertThat(encoder.matches("234", acc.getPassword())).isTrue();
        }
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_salary_history_by_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryHistory")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_salary_by_date_by_employee() throws Exception {
        SalaryId salaryId = new SalaryId();
        salaryId.setYear(2022);
        salaryId.setMonth(5);
        salaryId.setEmployee_id(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryByDate")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryId)))
                .andExpect(status().isOk());


    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_salary_by_invalid_date_by_employee() throws Exception {
        SalaryId salaryId = new SalaryId();
        salaryId.setYear(2024);
        salaryId.setMonth(5);
        salaryId.setEmployee_id(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/salaryByDate")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SalaryNotFoundException))
                .andExpect(result -> assertEquals("this employee was not paid in the provided date", result.getResolvedException().getMessage()));

    }



    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expectedDataForVacationRequest.xml")
    public void test_employee_vacation_request() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/vacation")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_days_off_by_date_by_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/daysOff/month/5/year/2022")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void test_get_days_off_by_invalid_date_by_employee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/daysOff/month/5/year/2024")
                        .with(httpBasic("a.a", "123"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SalaryNotFoundException))
                .andExpect(result -> assertEquals("this employee was not paid in the provided date", result.getResolvedException().getMessage()));
    }


}
