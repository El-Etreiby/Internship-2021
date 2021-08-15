package com.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//com.employees.controllers are for defining(mapping) URLs, error handling & calling com.employees.services
@SpringBootApplication
public class EmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

//	@Bean
//	public EmployeeService employeeService() {
//		return new EmployeeService();
//	}
//	@Bean
//	public TeamService teamService() {
//		return new TeamService();
//	}
//	@Bean
//	public DepartmentService departmentService() {
//		return new DepartmentService();
//	}
//	@Bean
//	public EmployeeController employeeController() {
//		return new EmployeeController();
//	}
//	@Bean
//	public TeamController teamController() {
//		return new TeamController();
//	}
//	@Bean
//	public DepartmentController departmentController() {
//		return new DepartmentController();
//	}
//	@Bean
//	public EmployeeRepository employeeRepository() {
//		return new EmployeeRepository();
//	}
//	@Bean
//	public TeamRepository teamRepository() {
//		return new TeamRepository();
//	}
//	@Bean
//	public DepartmentRepository departmentRepository() {
//		return new DepartmentRepository();
//	}


}
