# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table friend (
  id                        integer primary key AUTOINCREMENT,
  user1id                   integer,
  user2id                   integer)
;

create table game (
  id                        integer primary key AUTOINCREMENT,
  user1id                   integer,
  user2id                   integer,
  winner_id                 integer,
  finished                  integer(1))
;

create table picture (
  id                        integer primary key AUTOINCREMENT,
  lat                       double,
  lng                       double,
  title                     varchar(255),
  description               varchar(255),
  height                    integer,
  width                     integer,
  accepted                  integer(1),
  updload_date              timestamp,
  create_user_id            integer)
;

create table round (
  id                        integer primary key AUTOINCREMENT,
  game_id                   integer,
  picture_id                integer,
  user1distance             double,
  user2distance             double,
  winner_id                 integer,
  finished                  integer(1))
;

create table user (
  id                        integer primary key AUTOINCREMENT,
  name                      varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  active                    integer(1))
;




# --- !Downs

PRAGMA foreign_keys = OFF;

drop table friend;

drop table game;

drop table picture;

drop table round;

drop table user;

PRAGMA foreign_keys = ON;

