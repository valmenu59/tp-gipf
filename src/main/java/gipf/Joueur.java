package gipf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Joueur {
	private final String login;
	private String email;
	private String password;
	private int elo;

	public Joueur(String login, String email, String password, int elo) {
		super();
		this.login = login;
		this.email = email;
		this.password = password;
		this.elo = elo;
	}

	public int getElo() {
		return elo;
	}

	public void addElo(int elo) {
		this.elo += elo;
	}

	public String getLogin() {
		return login;
	}

	@Override
	public String toString() {
		return "Joueur [login=" + login + ", elo=" + elo + "]";
	}

	public void save(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			stmt.executeUpdate("UPDATE Joueur SET elo = " + elo + ", password = '" + password + "', email = '" + email
					+ "' WHERE login = '" + login + "';");
		}
	}

	public static Joueur inscrire(String login, String password, String email, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("INSERT INTO Joueur VALUES ('" + login + "', DEFAULT, '" + password
					+ "', '" + email + "') RETURNING *");
			if (!rs.next()) {
				throw new IllegalStateException("Aucune donnée insérée à l'inscription de " + login);
			}
			int elo = rs.getInt("elo");
			return new Joueur(login, email, password, elo);

		}
	}

	public static Joueur load(String login, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Joueur WHERE login = '" + login + "'");
			if (!rs.next()) {
				throw new IllegalArgumentException("L'utilisateur " + login + " n'existe pas");
			}
			int elo = rs.getInt("elo");
			String email = rs.getString("email");
			String pwd = rs.getString("password");
			return new Joueur(login, email, pwd, elo);
		}
	}

	public static Map<Joueur, Integer> loadByPartiesJouees(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(
					"WITH Played AS (                " + "  SELECT idPartie, blanc AS login FROM Partie          "
							+ "  UNION                                                "
							+ "  SELECT idPartie, noir AS login FROM Partie)          "
							+ "SELECT Joueur.*, count(idPartie)                       "
							+ "FROM Joueur LEFT JOIN Played USING (login)             "
							+ "GROUP BY login                                         "
							+ "ORDER BY count(idPartie) DESC                          ");

			Map<Joueur, Integer> data = new LinkedHashMap<>();
			while (rs.next()) {
				int elo = rs.getInt("elo");
				String email = rs.getString("email");
				String pwd = rs.getString("password");
				String login = rs.getString("login");
				int count = rs.getInt("count");
				data.put(new Joueur(login, email, pwd, elo), count);
			}
			return data;
		}
	}

	public static Map<Joueur, Integer> loadByPartiesGagnees(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt
					.executeQuery("SELECT Joueur.*, count(idPartie) FROM Joueur LEFT JOIN Partie ON login = gagnant "
							+ "GROUP BY login ORDER BY count(idPartie) DESC");
			Map<Joueur, Integer> data = new LinkedHashMap<>();
			while (rs.next()) {
				int elo = rs.getInt("elo");
				String email = rs.getString("email");
				String pwd = rs.getString("password");
				String login = rs.getString("login");
				int count = rs.getInt("count");
				data.put(new Joueur(login, email, pwd, elo), count);
			}
			return data;
		}
	}

	public static List<Joueur> loadByElo(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Joueur ORDER BY elo DESC");
			ArrayList<Joueur> data = new ArrayList<>();
			while (rs.next()) {
				int elo = rs.getInt("elo");
				String email = rs.getString("email");
				String pwd = rs.getString("password");
				String login = rs.getString("login");
				data.add(new Joueur(login, email, pwd, elo));
			}
			return data;
		}
	}

}
