create table person (
                       id bigint not null auto_increment primary key,
                       firstname varchar(100) not null,
                       lastname varchar(100) not null
);

insert into person (firstname, lastname) values ('Dan', 'Johnson');
insert into person (firstname, lastname) values ('Tom', 'Nilson');
insert into person (firstname, lastname) values ('Jim', 'Kerry');
