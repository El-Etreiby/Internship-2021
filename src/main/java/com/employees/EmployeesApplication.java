package com.employees;

import com.employees.services.HrService;
import com.employees.services.IssueSalaries;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

//com.employees.controllers are for defining(mapping) URLs, error handling & calling com.employees.services
@SpringBootApplication
public class EmployeesApplication {

    public static void main(String[] args) throws SchedulerException {
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
