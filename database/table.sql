create table commands(
	command VARCHAR(30) PRIMARY KEY,
 	response VARCHAR(1000) NOT NULL,
 	user VARCHAR(50) DEFAULT "USER MISSING",
	added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB;
