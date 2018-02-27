#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h> //open
#include <sys/stat.h> //open
#include <fcntl.h> //open
#include <unistd.h> //read/close
#include <string.h>

#define DOUBLE_DEB 1
#define DOUBLE_FIN 50

#define SIMPLE_DEB 51
#define SIMPLE_FIN 75

#define SIMPLE_2_DEB 76
#define SIMPLE_2_FIN 100

void main(int argc, char** argv)
{
	int fd_ecrire ;
	int i ;
	char requete1[255] ;
	char requete2[255] ;
	char requete3[255] ;


	fd_ecrire = open(argv[1], O_CREAT|O_TRUNC|O_RDWR, 0666) ;
	if (fd_ecrire == -1)
	{
		perror("open_ecrire") ;
		exit(1) ;
	}

	//Double
	for(i=DOUBLE_DEB ; i<=DOUBLE_FIN ; i++)
	{
		sprintf(requete1, "INSERT into `testes` values(%d, 2, '1er ou 2eme etage', 1, 0) ;\n", i) ;
		write(fd_ecrire, requete1, strlen(requete1)) ;
	}

	//Simple
	for(i=SIMPLE_DEB ; i<=SIMPLE_FIN ; i++)
	{
		sprintf(requete1, "INSERT into `testes` values(%d, 1, '3eme etage', 1, 0) ;\n", i) ;
		write(fd_ecrire, requete1, strlen(requete1)) ;
	}

	//2 simples
	for(i=SIMPLE_2_DEB ; i<=SIMPLE_2_FIN ; i++)
	{
		sprintf(requete1, "INSERT into `testes` values(%d, 3, '4eme etage', 1, 0) ;\n", i) ;
		write(fd_ecrire, requete1, strlen(requete1)) ;
	}

	close(fd_ecrire) ;
}