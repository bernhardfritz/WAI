# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table picture (
  id                        integer primary key AUTOINCREMENT,
  path                      varchar(255),
  lat                       double,
  lng                       double,
  title                     varchar(255),
  description               varchar(255),
  accepted                  integer(1),
  updload_date              timestamp)
;

create table user (
  id                        integer primary key AUTOINCREMENT,
  name                      varchar(255),
  email                     varchar(255),
  password                  varchar(255))
;




# --- !Downs

PRAGMA foreign_keys = OFF;

drop table picture;

drop table user;

PRAGMA foreign_keys = ON;

