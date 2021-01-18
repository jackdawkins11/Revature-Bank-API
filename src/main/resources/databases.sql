create table Roles(
	id serial primary key,
	role varchar(100) unique not null
);

create table Users(
	id serial primary key,
	username varchar(100) unique not null,
	password varchar(100) not null,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	email varchar(200) unique not null,
	role_id integer not null references Roles(id)
);

create table AccountStatus(
	id serial primary key,
	status varchar(100) unique not null
);

create table AccountType(
	id serial primary key,
	type varchar(100) unique not null
);

create table Account(
	id serial primary key,
	amount double precision,
	type_id integer references AccountType(id),
	status_id integer references AccountStatus(id)
);

create table UserAccount(
	user_id integer references Users(id),
	account_id integer references Account(id),
	primary key (user_id, account_id)
);