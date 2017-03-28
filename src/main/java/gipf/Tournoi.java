package gipf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournoi {
	private final int idTournoi;
	private final LocalDate debut;
	private LocalDate fin;

	private final String lieu;

	private List<Joueur> arbitres;

	public Tournoi(int idTournoi, LocalDate debut, LocalDate fin, String lieu) {
		super();
		this.idTournoi = idTournoi;
		this.debut = debut;
		this.fin = fin;
		this.lieu = lieu;
		this.arbitres = new ArrayList<>();
	}

	public static Tournoi create(LocalDate debut, LocalDate fin, String lieu, List<Joueur> arbitres, Connection con)
			throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(
					"INSERT INTO Tournoi VALUES (DEFAULT, '" + debut + "', '" + fin + "', '" + lieu + "') RETURNING *");
			rs.next();
			int id = rs.getInt("idTournoi");
			for (Joueur j : arbitres) {
				stmt.executeUpdate("INSERT INTO Arbitre VALUES ('" + j.getLogin() + "', " + id + ")");
			}
			Tournoi t = new Tournoi(id, debut, fin, lieu);
			t.arbitres.addAll(arbitres);
			return t;
		}
	}

	@Override
	public String toString() {
		return "Tournoi [idTournoi=" + idTournoi + ", debut=" + debut + ", fin=" + fin + ", lieu=" + lieu
				+ ", arbitres=" + arbitres + "]";
	}

	public int getIdTournoi() {
		return idTournoi;
	}

}
