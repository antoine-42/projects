create table `Chambre`
(
	Numero int not null,
	Categorie int not null,
	Description varchar(255),
	Nettoyee boolean not null,
	Occupee boolean not null,

	primary key (Numero)
) ;

#Première version Historique
create table `Historique`
(
	jour date not null,
	taux_occupation float,
	taux_non_presentation float,

	primary key (jour)
) ;

#Deuxième table créée avec un identificateur
create table `Historique`
(
	id int not null auto_increment,
	jour date not null,
	taux_occupation float,
	taux_non_presentation float,

	primary key (id)
) ;

create table `Presentations`
(
	Jour date not null,
	Reservation varchar(20),
	

	primary key (jour)
) ;


#Première version de l'évènement
create event save_history
ON SCHEDULE EVERY 1 DAY STARTS '2017-10-20 00:01:00'
do
	INSERT into bohl.Historique values
	(
			(select DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)),
			(select COUNT(*) from bohl.Chambre where Occupee=1),
			(select count(*) from bohl.Presentations where jour=(select DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)))
	)

create event save_history
ON SCHEDULE EVERY 1 DAY STARTS '2017-11-20 00:01:00'
do
	INSERT into bohl.Historique(jour, taux_occupation, taux_non_presentation) values
	(
			(select DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)),
			(select COUNT(*) from bohl.Chambre where Occupee=1),
			(select count(*) from bohl.Presentations where jour=(select DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)))
	)

