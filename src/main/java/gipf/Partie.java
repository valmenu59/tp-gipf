package gipf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Optional;

public class Partie {

	private final int idPartie;
	private final Instant date;
	private Optional<Integer> piecesRestantes;

	private final Joueur blanc;
	private final Joueur noir;
	private Optional<Joueur> gagnant;
	private Optional<Joueur> perdant;

	private Optional<Tournoi> tournoi;

	public Partie(int idPartie, Instant date, Joueur blanc, Joueur noir) {
		super();
		this.idPartie = idPartie;
		this.date = date;
		this.blanc = blanc;
		this.noir = noir;
		piecesRestantes = Optional.empty();
		gagnant = Optional.empty();
		perdant = Optional.empty();
		tournoi = Optional.empty();
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
			return new Partie(id, date, blanc, noir);
		}
	}

	public void setTournoi(Tournoi t) {
		tournoi = Optional.of(t);
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
			final String tourn = tournoi.map(t -> Integer.toString(t.getIdTournoi())).orElse("NULL");

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

}
