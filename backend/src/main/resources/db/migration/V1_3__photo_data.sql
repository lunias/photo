drop table PHOTO_DATA IF EXISTS;

create table PHOTO_DATA (
  id binary not null,
  batch_id varchar(255),
  derived_path varchar(255),
  extension varchar(255),
  filename varchar(255),
  name varchar(255),
  raw_path varchar(255),
  type varchar(16),
  primary key (id)
);