DROP TABLE tb_user;
CREATE TABLE tb_user (
	id		varchar(20) PRIMARY KEY,
	password	char(41) NOT NULL,
	name		varchar(20) DEFAULT '',
	cellphone	varchar(20) DEFAULT '',
	email		varchar(32) DEFAULT '',
	level		int
);
