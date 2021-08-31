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
    employee_id int not null primary key auto_increment,
    dob             datetime(6)  null,
    first_name   varchar(255) null,
    last_name   varchar(255) null,
    expertise       varchar(255) null,
    gender          char         not null,
    graduation_date datetime(6)  null,
    gross_salary    double       null,
    days_off_taken  int,
    national_id     bigint,
    years_of_experience int,
    degree varchar(255),
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

create table salary
(
    month int,
    year int,
    employee_id int,
    gross_salary double,
    net_salary double,
    bonus double,
    raise double,
    leave_deductions double,
    taxes double,
    primary key(month, year, employee_id),
    constraint FK8d7lrsr6kwirr93rx0tafppoqa
        foreign key (employee_id) references employee (employee_id)
);

create table account_information
(
    username varchar(255) primary key,
    password varchar(255),
    employee_id int,
    constraint FK8d7lrsr6kwidsr93rx0tafnoqa
        foreign key (employee_id) references employee (employee_id)
);
