package com.employees;

import com.employees.models.Department;
import com.employees.models.Employee;
import com.employees.repositories.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.text.SimpleDateFormat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest(EmployeeController.class)
//@SpringBootTest


@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void test_employee_creation_and_insertion() throws Exception {
       // this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        Employee newEmployee = new Employee();
        Department newDepartment = new Department();
        newDepartment.setDepartmentName("web development");
        departmentRepository.save(newDepartment);
        newEmployee.setName("A");
        newEmployee.setEmployeeId(1);
        newEmployee.setGender('M');
        newEmployee.setGrossSalary(10000.0);
        newEmployee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2000"));
        newEmployee.setGraduationDate(new SimpleDateFormat("dd/MM/yyyy").parse("1/1/2018"));
        newEmployee.setDepartment(newDepartment);
        newEmployee.setNetSalary(9000.0);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/addNewEmployee")
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

}
