# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table picture (
  data                      blob,
  lat                       double,
  lng                       double,
  description               varchar(255))
;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists picture;

SET REFERENTIAL_INTEGRITY TRUE;

