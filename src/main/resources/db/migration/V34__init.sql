  create table department
(
    department_id int not null primary key auto_increment,
    department_name varchar(255) not null unique
);
create table team
(
    team_id int not null primary key auto_increment,
    team_name varchar(255) not null unique
);
create table employee
(
    employee_id int not null primary key auto_increment,
    dob             datetime(6) not null,
    hire_date             datetime(6) not null,
    first_name   varchar(255) not null,
    last_name   varchar(255) not null,
    expertise       text,
    gender          varchar(255)        not null,
    graduation_date datetime(6),
    gross_salary    double   not null,
    days_off_taken  int default 0,
    national_id     bigint not null unique,
    years_of_experience int default 0,
    degree varchar(255) not null,
    bonus double default 0.0,
    raise double default 0.0,
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
    gross_salary double not     null,
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
    account_id int not null primary key auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not     null,
    role varchar(255) default 'EMPLOYEE',
    employee_id int,
    constraint FK8d7lrsr6kwidsr93rx0tafnoqa
        foreign key (employee_id) references employee (employee_id)
);
