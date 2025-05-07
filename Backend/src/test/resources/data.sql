-- create table
CREATE TABLE IF NOT EXISTS car (
    immatriculation VARCHAR(10) PRIMARY KEY,
    marque VARCHAR(10),
    modele VARCHAR(10),
    etat VARCHAR(10)
    );
-- insert data
insert into car (immatriculation,marque, modele, etat) values ('DZ-568-KC','toyota','yaris','neuve');
insert into car (immatriculation, marque, modele, etat) values ('DZ-569-KD','mercedes','benz','ocasion');
insert into car (immatriculation, marque, modele, etat) values ('LM-276-PV','Ford','fiesta','usage');

