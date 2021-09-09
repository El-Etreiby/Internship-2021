package com.employees.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class SalaryId implements Serializable {

    private int month;
    private int year;
    private Integer employee_id;

    public SalaryId(){

    }
    public SalaryId(Integer employeeId, int month, int year){
        this.year=year;
        this.month=month;
        this.employee_id=employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        SalaryId salaryId = (SalaryId) o;
        return this.employee_id.equals(salaryId.employee_id) &&
                this.month==(salaryId.month) &&
                this.year==(salaryId.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee_id, month, year);
    }
}
