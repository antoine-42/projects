horsdoeuvre(artichauts ).
horsdoeuvre(crevettes ).
horsdoeuvre(oeufs ).
viande(grillade-de-boeuf).
viande(poulet).
poisson(loup).
poisson(sole).
dessert(glace).
dessert(tarte).
dessert(fraises ).

plat(X) :- viande(X).
plat(Y) :- poisson(Y).

repas(X,Y,Z) :- horsdoeuvre(X), plat(Y), dessert(Z).

calories( artichauts , 150).
calories( crevettes , 250).
calories( oeufs , 200).
calories( grillade-de-boeuf , 500).
calories( poulet , 430).
calories( loup , 250).
calories( sole , 200).
calories( glace , 300).
calories( tarte , 400).
calories( fraises , 250).

cal-repas(E, P, D, C) :- repas(E, P, D), calories(E, CE),
			 calories(P, CP), calories(D, CD),
			 C is CE + CP + CD.

repas-equilibre(E, P, D) :- cal-repas(E, P, D, C), C < 900.

boisson(vin).
boisson(eau).
boisson(biere).

repas-complet(E,P,D,B) :- repas(E,P,D), boisson(B).
