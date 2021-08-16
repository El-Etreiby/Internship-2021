package com.employees.models;


import com.employees.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void test_add_new_employee(){
          // arrange
        Employee newEmployee = new Employee();
        newEmployee.setGender('F');
        newEmployee.setGrossSalary(20000.0);
        newEmployee.setEmployeeName("s");
//        // act
        String response =  employeeService.addNewEmployee(newEmployee);
//        // assert

        assertEquals("Employee added successfully!",response);
    }

    @Test
    public void test_remove_employee() throws Exception {
        // arrange

//        // act
        String response =  employeeService.removeEmployee(6);
//        // assert

        assertEquals("Employee deleted successfully!",response);
    }
}
