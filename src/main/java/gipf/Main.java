package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.postgresql.ds.PGSimpleDataSource;

public class Main {
	private static final Random RAND = new Random(0);

	/**
	 * Suppression de toutes les données
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private static void clean(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			stmt.executeUpdate("TRUNCATE TABLE Partie, Arbitre, Tournoi, Joueur");
		}
	}

	/**
	 * Inscription d'une liste de joueurs avec mot de passe aléatoire
	 * 
	 * (Question 2)
	 * 
	 * @param usernames
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private static List<Joueur> inscrire(List<String> usernames, Connection con) throws SQLException {
		List<Joueur> joueurs = new ArrayList<>();
		for (String username : usernames) {
			joueurs.add(
					Joueur.inscrire(username, UUID.randomUUID().toString(), username + "@univ-valenciennes.fr", con));
		}
		return joueurs;
	}

	/**
	 * Affichage du classement ELO (Question 4)
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private static void classementElo(Connection con) throws SQLException {
		for (Joueur j : Joueur.loadByElo(con)) {
			System.out.println(j);
		}
		System.out.println();
	}

	/**
	 * Affichage du classement par nombre de parties gagnées (Question 5)
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private static void classementPartiesGagnees(Connection con) throws SQLException {
		for (Entry<Joueur, Integer> j : Joueur.loadByPartiesGagnees(con).entrySet()) {
			System.out.println(j);
		}
		System.out.println();
	}

	/**
	 * Affichage du classement par nombre de parties jouées (Question 8)
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private static void classementPartiesJouees(Connection con) throws SQLException {
		for (Entry<Joueur, Integer> j : Joueur.loadByPartiesJouees(con).entrySet()) {
			System.out.println(j);
		}
		System.out.println();
	}

	public static void main(String[] args) throws SQLException {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setServerName("localhost");
		ds.setDatabaseName("gipf");

		try (Connection con = ds.getConnection("vion", "vion")) {

			clean(con);

			List<Joueur> joueurs = inscrire(Arrays.asList("baroqueen", "cobrag", "vikingkong", "preaster",
					"fickleSkeleton", "SnowTea", "AfternoonTerror", "JokeCherry", "JealousPelican", "PositiveLamb"),
					con);

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
			classementElo(con);

			System.out.println("*** Classement parties gagnées (Question 5) ***");
			classementPartiesGagnees(con);

			// Enregistrement d'un tournoi (Question 6)
			// Sélection de trois arbitres dans la liste des joueurs
			List<Joueur> arbitres = joueurs.subList(0, 3);

			Tournoi t = Tournoi.create(LocalDate.of(2017, 3, 10), LocalDate.of(2017, 3, 12), "Maubeuge", arbitres, con);
			System.out.println(t);

			// Affectation des 10 premières parties au tournoi (Question 7)
			for (Partie p : parties.subList(0, 10)) {
				p.setTournoi(t);
				p.save(con);
			}

			System.out.println("*** Classement parties jouées (Question 8) ***");
			classementPartiesJouees(con);
		}
	}
}
