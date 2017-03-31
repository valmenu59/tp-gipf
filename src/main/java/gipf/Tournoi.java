package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Représente un tournoi
 */
public class Tournoi {

	/**
	 * @return la date de fin du tournoi si elle est connue
	 */
	public Optional<LocalDate> getFin() {
		throw new NotImplementedError();
	}

	/**
	 * Modifie la date de fin
	 */
	public void setFin(LocalDate fin) {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return la liste des arbitres
	 */
	public Set<Joueur> getArbitres() {
		throw new NotImplementedError();
	}

	/**
	 * Ajoute un arbitre au tournoi
	 * 
	 * @param j
	 */
	public void addArbitre(Joueur j) {
		throw new NotImplementedError();
	}

	/**
	 * Supprime un arbitre du tournoi
	 * 
	 * @param j
	 */
	public void removeArbitre(Joueur j) {
		throw new NotImplementedError();
	}

	/**
	 * @return la date de début du tournoi
	 */
	public LocalDate getDebut() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le lieu du tournoi
	 */
	public String getLieu() {
		throw new NotImplementedError();
	}

	/**
	 * Crée un tournoi avec les paramètres indiqués et l'enregistre dans la base
	 * 
	 * @param debut
	 * @param lieu
	 * @param arbitres
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static Tournoi create(LocalDate debut, String lieu, List<Joueur> arbitres, Connection con)
			throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Charge un tournoi depuis la base à partir de son identifiant
	 * 
	 * @param idTournoi
	 * @param con
	 * @return le tournoi s'il existe, les arbitres sont également chargés
	 * @throws SQLException
	 */
	public static Optional<Tournoi> load(int idTournoi, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Charge les parties d'un tournoi
	 * 
	 * @param con
	 * @return les parties chargées du tournoi
	 * @throws SQLException
	 */
	public List<Partie> loadParties(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	@Override
	public String toString() {
		return "Tournoi [idTournoi=" + getIdTournoi() + ", debut=" + getDebut() + ", fin=" + getFin() + ", lieu="
				+ getLieu() + ", arbitres=" + getArbitres() + "]";
	}

	/**
	 * 
	 * @return l'identifiant du tournoi
	 */
	public int getIdTournoi() {
		throw new NotImplementedError();
	}

	/**
	 * Sauvegarde le tournoi dans la base de données. La date de fin et la liste
	 * des arbitres peuvent avoir été modifiées.
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void save(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

}
