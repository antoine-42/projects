%% fact(0, 1).
%% fact(N, F) :- N > 0, N1 is N - 1, fact(N1, F1), F is F1 * N.

fact-aux(0, A, A).
fact-aux(N, A, R) :- N > 0, N1 is N - 1, A1 is A * N, fact-aux(N1, A1, R).
fact(N,R) :- fact-aux(N, 1, R).

division(A,B,Q,R) :- A >= 0, A < B, Q is 0, R is A.
division(A,B,Q,R) :- A >= B, A1 is A - B, division(A1, B, Q1, R), Q is Q1 + 1.

longueur([], 0).
longueur([_|Q], N) :- longueur(Q, M), N is M + 1.

sumlist([], 0).
sumlist([X | Q], N) :- sumlist(Q, S), N is X + S.

sorted([]).
sorted([_]).
sorted([X, Y | R]) :- X =< Y, sorted([Y | R]).

merge([], L, L).
merge(L, [], L).
merge([X | Q], [Y | R], [X | L]) :- X =< Y, merge(Q, [Y | R], L).
merge([X | Q], [Y | R], [Y | L]) :- X > Y, merge([X | Q], R, L).


max(X,Y,X) :- X >= Y.
max(X,Y,Y) :- X < Y.

max2(X,Y,X) :- X >= Y, !.
max2(X,Y,Y).
