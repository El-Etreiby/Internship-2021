//package com.employees.models;
//
//import javax.persistence.*;
//import java.time.Month;
//import java.time.Year;
//
//@Entity
//@IdClass(AttendanceId.class)
//public class Attendance {
//
//    @Id
//    private Short day;
//
//    @Id
//    private Month month;
//
//    @Id
//    private Year year;
//
//    @Id
//    private Integer employeeId;
//
//    @Id
//    @OneToOne
//    @JoinColumn(name="employee_id")
//    private Employee employee;
//
//    public Short getDay() {
//        return day;
//    }
//
//    public void setDay(Short day) {
//        this.day = day;
//    }
//
//    public Month getMonth() {
//        return month;
//    }
//
//    public void setMonth(Month month) {
//        this.month = month;
//    }
//
//    public Year getYear() {
//        return year;
//    }
//
//    public void setYear(Year year) {
//        this.year = year;
//    }
//
//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }
//}
