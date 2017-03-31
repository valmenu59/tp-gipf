package gipf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Partie {

	private final int idPartie;
	private final Instant date;
	private Optional<Integer> piecesRestantes;

	private final Joueur blanc;
	private final Joueur noir;
	private Optional<Joueur> gagnant;
	private Optional<Joueur> perdant;

	private Optional<Integer> idTournoi;

	private Partie(int idPartie, Instant date, Joueur blanc, Joueur noir, Optional<Joueur> gagnant,
			Optional<Joueur> perdant, Optional<Integer> piecesRestantes, Optional<Integer> idTournoi) {
		super();
		this.idPartie = idPartie;
		this.date = date;
		this.piecesRestantes = piecesRestantes;
		this.blanc = blanc;
		this.noir = noir;
		this.gagnant = gagnant;
		this.perdant = perdant;
		this.idTournoi = idTournoi;
	}

	public int getIdPartie() {
		return idPartie;
	}

	public Instant getDate() {
		return date;
	}

	public Optional<Integer> getPiecesRestantes() {
		return piecesRestantes;
	}

	public Joueur getBlanc() {
		return blanc;
	}

	public Joueur getNoir() {
		return noir;
	}

	public Optional<Joueur> getGagnant() {
		return gagnant;
	}

	public Optional<Joueur> getPerdant() {
		return perdant;
	}

	public Optional<Integer> getIdTournoi() {
		return idTournoi;
	}

	@Override
	public String toString() {
		return "Partie [idPartie=" + idPartie + ", date=" + date + ", piecesRestantes=" + piecesRestantes + ", blanc="
				+ blanc + ", noir=" + noir + ", gagnant=" + gagnant + ", perdant=" + perdant + "]";
	}

	public static Partie create(Joueur blanc, Joueur noir, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("INSERT INTO Partie VALUES (DEFAULT, DEFAULT, NULL, '" + blanc.getLogin()
					+ "', '" + noir.getLogin() + "', NULL, NULL, NULL) RETURNING *");
			if (!rs.next()) {
				throw new IllegalStateException("Aucune donnée insérée à l'enregistrement de la partie");
			}
			int id = rs.getInt("idPartie");
			Instant date = rs.getTimestamp("datePartie").toInstant();
			return new Partie(id, date, blanc, noir, Optional.empty(), Optional.empty(), Optional.empty(),
					Optional.empty());
		}
	}

	public void setTournoi(Tournoi t) {
		idTournoi = Optional.of(t.getIdTournoi());
	}

	public static Optional<Partie> load(int idPartie, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Partie WHERE idPartie = " + idPartie);
			return load(rs, con).stream().findAny();
		}
	}

	private static List<Partie> load(ResultSet rs, Connection con) throws SQLException {
		List<Partie> parties = new ArrayList<>();
		while (rs.next()) {
			int idPartie = rs.getInt("idPartie");
			Instant date = rs.getTimestamp("datePartie").toInstant();
			String loginBlanc = rs.getString("blanc");
			String loginNoir = rs.getString("noir");
			Optional<Integer> piecesRestantes = Optional.ofNullable(rs.getObject("piecesRestantes"))
					.map(o -> (Integer) o);
			Optional<String> loginGagnant = Optional.ofNullable(rs.getString("gagnant"));
			Optional<String> loginPerdant = Optional.ofNullable(rs.getString("perdant"));
			Optional<Integer> idTournoi = Optional.ofNullable(rs.getObject("idTournoi")).map(o -> (Integer) o);

			Joueur blanc = Joueur.load(loginBlanc, con).get();
			Joueur noir = Joueur.load(loginNoir, con).get();

			Optional<Joueur> gagnant = loginGagnant.map(j -> j.equals(loginBlanc) ? blanc : noir);
			Optional<Joueur> perdant = loginPerdant.map(j -> j.equals(loginBlanc) ? blanc : noir);

			parties.add(new Partie(idPartie, date, blanc, noir, gagnant, perdant, piecesRestantes, idTournoi));
		}
		return parties;
	}

	public static List<Partie> loadTournoi(int idTournoi, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Partie WHERE idTournoi = " + idTournoi);
			return load(rs, con);
		}
	}

	public static Map<Joueur, Integer> classementPartiesJouees(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("WITH Played AS (                      "
					+ "  SELECT idPartie, blanc AS login FROM Partie                 "
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

	public static Map<Joueur, Integer> classementPartiesGagnees(Connection con) throws SQLException {
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

	public void save(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			final String pr;
			if (piecesRestantes.isPresent()) {
				pr = piecesRestantes.get().toString();
			} else {
				pr = "NULL";
			}

			// Equivalent en utilisant les syntaxes Java 8
			final String g = gagnant.map(j -> "'" + j.getLogin() + "'").orElse("NULL");
			final String p = perdant.map(j -> "'" + j.getLogin() + "'").orElse("NULL");
			final String tourn = idTournoi.map(id -> Integer.toString(id)).orElse("NULL");

			stmt.executeUpdate("UPDATE Partie SET piecesRestantes = " + pr + ", gagnant = " + g + ", perdant = " + p
					+ ", idTournoi = " + tourn + " WHERE idPartie = " + idPartie);
		}
	}

	public void setGagnant(boolean blancGagne, int piecesRestantes, Connection con) throws SQLException {
		final Joueur g;
		final Joueur p;
		if (blancGagne) {
			g = blanc;
			p = noir;
		} else {
			g = noir;
			p = blanc;
		}
		gagnant = Optional.of(g);
		perdant = Optional.of(p);
		this.piecesRestantes = Optional.of(piecesRestantes);
		final int score = (int) (32 * (1 - 1 / (1 + Math.pow(10, (p.getElo() - g.getElo()) / 400))));
		g.addElo(score);
		p.addElo(-score);
		g.save(con);
		p.save(con);
		save(con);
	}

	public boolean equals(Object o) {
		if (o instanceof Partie) {
			return idPartie == ((Partie) o).getIdPartie();
		} else {
			return false;
		}
	}

	public int hashCode() {
		return idPartie;
	}

}
