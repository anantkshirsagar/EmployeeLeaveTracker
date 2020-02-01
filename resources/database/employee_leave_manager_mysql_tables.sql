#create database if not exists employee_leave_manager;

create table if not exists leave_request(
id bigint primary key auto_increment,
from_date date,
to_date date,
days int,
employee_id int,
leave_reason varchar(255),
approve_status int
);

create table if not exists holiday_record(
id bigint primary key auto_increment,
holiday_name varchar(1024),
holiday_date date
);

create table if not exists current_year_record(
id bigint primary key auto_increment,
current_year int,
total_leaves int
);

create table if not exists employee(
id bigint primary key auto_increment,
name varchar(255),
city varchar(255),
email varchar(255),
password varchar(255),
contact_no varchar(255),
is_manager boolean,
approved_leaves_count int,
pending_leaves_count int,
remaining_leaves_count int
);