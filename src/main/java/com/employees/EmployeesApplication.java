package com.employees;

import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//com.employees.controllers are for defining(mapping) URLs, error handling & calling com.employees.services
@SpringBootApplication
public class EmployeesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//        scheduler.start();
//
//        JobDetail job = newJob(IssueSalaries.class)
//                .withIdentity("issue-salaries")
//                .build();
//
//        SimpleTrigger trigger = newTrigger().withIdentity("trigger")
//                .startNow()
//                .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever())
//                .build();
//
//        scheduler.scheduleJob(job, trigger);
    }


}
