app([], L, L).
app([X | Q], L, [X | Z]) :- app(Q, L, Z).

premier(X,[X|_]).

%% premier(X,L) :- app([X], Q, L).

%% dernier(X,L) :- app(Q, [X], L).

dernier(X, [X]).
dernier(X, [_ | Q]) :- dernier(X, Q).

mem(X, [X | _]).
mem(X, [_ | Q]) :- mem(X, Q).

double([], []).
double([X | Q], [X, X | L]) :- double(Q, L).

longueurpaire([]).
%% longueurimpaire([_|S]) :- longueurpaire(S).
longueurpaire([_, _ |S]) :- longueurpaire(S).

%% rev([], []).
%% rev([X| Q], L) :- rev(Q, R), app(R, [X], L).

rev-aux([], A, A).
rev-aux([X | Q], A, L) :- rev-aux(Q, [X | A], L).

rev(L1, L2) :- rev-aux(L1, [], L2).

%% prefixe(L1, L2) :- app(L1, _, L2).

prefixe([], _).
prefixe([X | Q], [X | R]) :- prefixe(Q, R).

palindrome(L) :- rev(L, L).

%% inserer(X, L, R) est vrai si R est obtenue en insérant
%% X quelque part dans L.
inserer(X, L, [X | L]).
inserer(X, [Y | Q], [Y | R])  :- inserer(X, Q, R).

perm([], []).
perm([X | Q], R) :- perm(Q, P), inserer(X, P, R).
