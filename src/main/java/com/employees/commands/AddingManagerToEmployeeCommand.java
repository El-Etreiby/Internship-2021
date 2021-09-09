package com.employees.commands;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddingManagerToEmployeeCommand implements Serializable {
    private Integer employeeId;
    private Integer managerId;

}
