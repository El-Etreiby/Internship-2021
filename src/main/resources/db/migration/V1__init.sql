--CREATE SCHEMA IF NOT EXISTS public;
--SET SCHEMA public;

create table department
(
    department_id int not null primary key,
    department_name varchar(255) null
);
create table team
(
    team_id int not null primary key,
    team_name varchar(255) null
);
create table employee
(
    employee_id int not null primary key,
    dob             datetime(6)  null,
    employee_name   varchar(255) null,
    expertise       varchar(255) null,
    gender          char         not null,
    graduation_date datetime(6)  null,
    gross_salary    double       null,
    net_salary      double       null,
    department_id   int          null,
    team_id         int          null,
    manager_id      int          null,
    constraint FK8d7lrsr6kwirr93rx0tafnoqa
        foreign key (team_id) references team (team_id),
    constraint FKbejtwvg9bxus2mffsm3swj3u9
        foreign key (department_id) references department (department_id),
    constraint FKou6wbxug1d0qf9mabut3xqblo
        foreign key (manager_id) references employee (employee_id)
);

create table hibernate_sequence(
    sequence_name VARCHAR,
    next_val INTEGER NOT NULL
);
INSERT INTO hibernate_sequence (next_val) VALUES (1);

--CREATE SEQUENCE EMPLOYEE_SEQUENCE_ID START WITH (select max(employee_id) + 1 from employee);