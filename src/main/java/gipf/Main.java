package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.postgresql.ds.PGSimpleDataSource;

public class Main {
	private static final Random RAND = new Random(0);

	public static void main(String[] args) throws SQLException {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setServerName("localhost");
		ds.setDatabaseName("gipf");

		try (Connection con = ds.getConnection("vion", "vion")) {

			try (Statement stmt = con.createStatement()) {
				// Suppression de toutes les données
				stmt.executeUpdate("TRUNCATE TABLE Partie, Arbitre, Tournoi, Joueur");
			}

			// Inscription de quelques joueurs avec mot de passe aléatoire
			// (Question 2)
			List<Joueur> joueurs = new ArrayList<>();
			for (String username : Arrays.asList("baroqueen", "cobrag", "vikingkong", "preaster", "fickleSkeleton",
					"SnowTea", "AfternoonTerror", "JokeCherry", "JealousPelican", "PositiveLamb")) {
				joueurs.add(Joueur.inscrire(username, UUID.randomUUID().toString(), username + "@univ-valenciennes.fr",
						con));
			}

			// Enregistrement de quelques parties (Question 3)
			List<Partie> parties = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				int j1 = RAND.nextInt(joueurs.size());
				int j2 = RAND.nextInt(joueurs.size());
				if (j1 != j2) {
					parties.add(Partie.create(joueurs.get(j1), joueurs.get(j2), con));
				}
			}

			// Sélection des victoires aléatoire pour les 30 premières parties
			// La mise à jour des scores ELO des joueurs est automatique
			for (Partie p : parties.subList(0, 30)) {
				p.setGagnant(RAND.nextBoolean(), RAND.nextInt(5), con);
			}

			// Affichages
			System.out.println("*** Classement ELO (Question 4) ***");
			for (Joueur j : Joueur.loadByElo(con)) {
				System.out.println(j);
			}
			System.out.println();

			System.out.println("*** Classement parties gagnées (Question 5) ***");
			for (Entry<Joueur, Integer> j : Joueur.loadByPartiesGagnees(con).entrySet()) {
				System.out.println(j);
			}
			System.out.println();

			// Enregistrement d'un tournoi (Question 6)
			// Sélection aléatoire de trois arbitres dans la liste des joueurs
			Collections.shuffle(joueurs, RAND);
			List<Joueur> arbitres = joueurs.subList(0, 3);

			Tournoi t = Tournoi.create(LocalDate.of(2017, 3, 10), LocalDate.of(2017, 3, 12), "Maubeuge", arbitres, con);
			System.out.println(t);

			// Affectation des 10 premières parties au tournoi (Question 7)
			for (Partie p : parties.subList(0, 10)) {
				p.setTournoi(t);
				p.save(con);
			}

			System.out.println("*** Classement parties jouées (Question 8) ***");
			for (Entry<Joueur, Integer> j : Joueur.loadByPartiesJouees(con).entrySet()) {
				System.out.println(j);
			}
			System.out.println();

		}
	}
}
