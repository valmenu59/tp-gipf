package gipf;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Tournoi {
	private final int idTournoi;
	private final LocalDate debut;
	private Optional<LocalDate> fin;

	private final String lieu;

	private Set<Joueur> arbitres;

	public Optional<LocalDate> getFin() {
		return fin;
	}

	public void setFin(LocalDate fin) {
		this.fin = Optional.of(fin);
	}

	public Set<Joueur> getArbitres() {
		return Collections.unmodifiableSet(arbitres);
	}

	public void addArbitre(Joueur j) {
		this.arbitres.add(j);
	}

	public void removeArbitre(Joueur j) {
		this.arbitres.remove(j);
	}

	public LocalDate getDebut() {
		return debut;
	}

	public String getLieu() {
		return lieu;
	}

	public Tournoi(int idTournoi, LocalDate debut, Optional<LocalDate> fin, String lieu, Set<Joueur> arbitres) {
		super();
		this.idTournoi = idTournoi;
		this.debut = debut;
		this.fin = fin;
		this.lieu = lieu;
		this.arbitres = new HashSet<Joueur>(arbitres);
	}

	public Tournoi(int idTournoi, LocalDate debut, String lieu) {
		this(idTournoi, debut, Optional.empty(), lieu, new HashSet<>());
	}

	public static Tournoi create(LocalDate debut, String lieu, List<Joueur> arbitres, Connection con)
			throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(
					"INSERT INTO Tournoi VALUES (DEFAULT, '" + debut + "', NULL, '" + lieu + "') RETURNING *");
			if (!rs.next()) {
				throw new IllegalStateException(
						"Aucune donnée insérée à la création du tournoi de " + lieu + " du " + debut);
			}
			int id = rs.getInt("idTournoi");
			for (Joueur j : arbitres) {
				stmt.executeUpdate("INSERT INTO Arbitre VALUES ('" + j.getLogin() + "', " + id + ")");
			}
			Tournoi t = new Tournoi(id, debut, lieu);
			t.arbitres.addAll(arbitres);
			return t;
		}
	}

	public static Optional<Tournoi> load(int idTournoi, Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(
					"SELECT Tournoi.*, array_agg(login) filter (where login is not null) FROM Tournoi LEFT JOIN Arbitre USING (idTournoi) WHERE idTournoi = "
							+ idTournoi + " GROUP BY idTournoi");
			if (!rs.next()) {
				return Optional.empty();
			} else {
				LocalDate dateDebut = rs.getDate("dateDebut").toLocalDate();
				Optional<LocalDate> dateFin = Optional.ofNullable(rs.getDate("dateFin")).map(Date::toLocalDate);
				String lieu = rs.getString("lieu");
				Array loginArbitres = rs.getArray("array_agg");

				Set<Joueur> arbitres = new HashSet<Joueur>();
				if (loginArbitres != null) {
					for (String l : (String[]) loginArbitres.getArray()) {
						arbitres.add(Joueur.load(l, con).get());
					}
				}

				return Optional.of(new Tournoi(idTournoi, dateDebut, dateFin, lieu, arbitres));

			}
		}
	}

	public List<Partie> loadParties(Connection con) throws SQLException {
		return Partie.loadTournoi(idTournoi, con);
	}

	@Override
	public String toString() {
		return "Tournoi [idTournoi=" + idTournoi + ", debut=" + debut + ", fin=" + fin + ", lieu=" + lieu
				+ ", arbitres=" + arbitres + "]";
	}

	public int getIdTournoi() {
		return idTournoi;
	}

	public void save(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			final String f = fin.map(d -> "'" + d.toString() + "'").orElse("NULL");

			stmt.executeUpdate("UPDATE Tournoi SET dateFin = " + f + " WHERE idTournoi = " + idTournoi);
			stmt.executeUpdate("DELETE FROM Arbitre WHERE idTournoi = " + idTournoi);
			for (Joueur j : arbitres) {
				stmt.executeUpdate("INSERT INTO Arbitre VALUES ('" + j.getLogin() + "', " + idTournoi + ")");
			}
		}
	}

}
