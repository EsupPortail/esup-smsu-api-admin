create table fonction (FCT_ID integer not null auto_increment, FTC_NAME varchar(32) not null unique, primary key (FCT_ID)) ENGINE=InnoDB;
create table role (ROL_ID integer not null auto_increment, ROL_NAME varchar(32) not null unique, primary key (ROL_ID)) ENGINE=InnoDB;
create table role_composition (ROL_ID integer not null, FCT_ID integer not null, primary key (ROL_ID, FCT_ID)) ENGINE=InnoDB;
create table user_bo_smsu (USER_ID integer not null auto_increment, USER_LOGIN varchar(32) not null unique, ROL_ID integer not null, primary key (USER_ID)) ENGINE=InnoDB;
alter table role_composition add index FK7545E7617AA0C267 (ROL_ID), add constraint FK7545E7617AA0C267 foreign key (ROL_ID) references role (ROL_ID);
alter table role_composition add index FK7545E761774B10BB (FCT_ID), add constraint FK7545E761774B10BB foreign key (FCT_ID) references fonction (FCT_ID);
alter table user_bo_smsu add index FK5DECDA7A7AA0C267 (ROL_ID), add constraint FK5DECDA7A7AA0C267 foreign key (ROL_ID) references role (ROL_ID);
