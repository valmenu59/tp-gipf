drop table if exists Arbitre, Partie, Tournoi, Joueur;
CREATE TABLE Joueur
(
    login    text PRIMARY KEY,
    elo      real NOT NULL DEFAULT 1000,
    password text NOT NULL,
    email    text NOT NULL UNIQUE CHECK (email ~ '@')
);

CREATE TABLE Tournoi
(
    idTournoi serial primary key,
    dateDebut date not null default now(),
    dateFin   date,
    check (dateFin >= dateDebut),
    lieu      text not null
);

CREATE TABLE Partie
(
    idPartie        serial primary key,
    datePartie      timestamp not null default now(),
    piecesRestantes int check (piecesRestantes >= 0),
    blanc           text      not null references Joueur,
    noir            text      not null references Joueur,
    gagnant         text references Joueur,
    perdant         text references Joueur,
    idTournoi       int references Tournoi,
    check (blanc != noir),
    check (gagnant = noir or perdant = noir),
    check (gagnant = blanc or perdant = blanc)
);

CREATE TABLE Arbitre
(
    login     text references Joueur,
    idTournoi int references Tournoi,
    primary key (login, idTournoi)
);
