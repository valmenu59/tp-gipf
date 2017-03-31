package gipf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public void setPassword(String s) {
		password = s;
	}

	public void setEmail(String s) {
		email = s;
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

	public static Joueur inscrire(String login, String password, String email, Connection con)
			throws SQLException, InscriptionException {
		try (PreparedStatement stmt = con
				.prepareStatement("INSERT INTO Joueur VALUES (?, DEFAULT, ?, ?) RETURNING *")) {
			stmt.setString(1, login);
			stmt.setString(2, password);
			stmt.setString(3, email);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new IllegalStateException("Aucune donnée insérée à l'inscription de " + login);
			}
			int elo = rs.getInt("elo");
			return new Joueur(login, email, password, elo);

		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "23505":
				throw new InscriptionException("Le login " + login + " ou l'email " + email + " existe déjà");
			case "23514":
				throw new InscriptionException("Erreur de contrôle des données : " + e.getLocalizedMessage());
			default:
				System.out.println(e.getSQLState());
				throw e;
			}
		}
	}

	public static Optional<Joueur> load(String login, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Joueur WHERE login = '" + login + "'");
			if (!rs.next()) {
				return Optional.empty();
			} else {
				int elo = rs.getInt("elo");
				String email = rs.getString("email");
				String pwd = rs.getString("password");
				return Optional.of(new Joueur(login, email, pwd, elo));
			}
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

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public boolean equals(Object o) {
		if (o instanceof Joueur) {
			return login.equals(((Joueur) o).login);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return login.hashCode();
	}

}
