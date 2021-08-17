package com.employees;

import com.employees.commands.AddingEmployeeToDepartmentCommand;
import com.employees.commands.AddingEmployeeToTeamCommand;
import com.employees.commands.AddingManagerToEmployeeCommand;
import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.models.Team;
import com.employees.repositories.DepartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest(EmployeeController.class)
//@SpringBootTest


@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    DepartmentRepository departmentRepository;

    @Test
    public void test_employee_creation_and_insertion() throws Exception {
       // this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeName("AAAA");
        newEmployee.setGender('M');
        newEmployee.setGrossSalary(10000.0);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk());


        //        // arrange
//        EmployeeController controller = new EmployeeController();
//        // act
//        Set<String> expertise = new HashSet();
//        expertise.add("web development");
//        expertise.add("backend");
//        String response =  controller.addNewEmployee("Ahmed",'M',new SimpleDateFormat("mm/dd/yyyy").parse("1/1/2000"),expertise,new SimpleDateFormat("mm/dd/yyyy").parse("1/1/2018"),10000.0,"web development");
//        // assert
//        assertEquals("Employee Saved!",response);
    }
   // @Transactional
    @Test
    public void test_delete_employee() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(7)))
                .andExpect(status().isOk());
    }

    @Test //fel get est3amel DTOs to return data w fel post e3mel command objects to send data

    public void test_add_employee_to_team() throws Exception {
        AddingEmployeeToTeamCommand command = new AddingEmployeeToTeamCommand();
        command.setEmployeeId(14);
        command.setTeamId(11);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/14/team/11")
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
        command.setEmployeeId(8);
        command.setManagerId(14);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/8/manager/14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_modify_employee() throws Exception { //doesnt handle updating manager, team or department
        Employee modifiedEmployee = new Employee();
//        modifiedEmployee.setEmployeeName("SsSsS");
//        modifiedEmployee.setGender('F');
        modifiedEmployee.setGrossSalary(1313.13);
        modifiedEmployee.setNetSalary();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/14")
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
                        .delete("/employee/8/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(8)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_get_all_employees() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
//at2aked ezay en el entities fe3lan related f java?????
}
