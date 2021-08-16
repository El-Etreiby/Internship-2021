package com.employees.models;


import com.employees.services.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void test_add_new_department(){
        //arrange
        Department newDepartment = new Department();
        newDepartment.setDepartmentName("Dep1");
        //act
        String response = departmentService.addNewDepartment(newDepartment);
        //assert
        assertEquals("Department added successfully!",response);
    }

    @Test
    public void test_delete_department() throws Exception {
        //arrange

        //act
        String response = departmentService.removeDepartment(3);
        //assert
        assertEquals("Department deleted successfully!",response);
    }
}
