//package com.employees.models;
//
//import java.io.Serializable;
//import java.time.Month;
//import java.time.Year;
//import java.util.Objects;
//
//public class AttendanceId implements Serializable {
//
//    private Short day;
//    private Month month;
//    private Year year;
//    private Integer employeeId;
//
//    public AttendanceId(){
//
//    }
//    public AttendanceId(Integer employeeId, Month month, Year year, Short day){
//        this.day=day;
//        this.year=year;
//        this.month=month;
//        this.employeeId=employeeId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o){
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()){
//            return false;
//        }
//        AttendanceId attendanceId = (AttendanceId) o;
//        return this.employeeId.equals(attendanceId.employeeId) &&
//                this.month.equals(attendanceId.month) &&
//                this.year.equals(attendanceId.year) &&
//                this.day.equals(attendanceId.day);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(employeeId, month, year,day);
//    }
//}
