package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Représente une partie, jouée ou simplement programmée, éventuellement liée à
 * un tournoi
 */
public class Partie {


	/**
	 * @return l'identifiant de la partie dans la base de données
	 */
	public int getIdPartie() {
		throw new NotImplementedError();
	}

	/**
	 * @return date et heure de la partie
	 */
	public LocalDateTime getDate() {
		throw new NotImplementedError();
	}

	/**
	 * Modifie les date et heure de la partie
	 */
	public void setDate(LocalDateTime date) {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le nombre de pièces restantes à l'issue de la partie s'il est
	 *         connu
	 */
	public Optional<Integer> getPiecesRestantes() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le joueur blanc
	 */
	public Joueur getBlanc() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le joueur noir
	 */
	public Joueur getNoir() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le joueur gagnant s'il est connu (même instance que le blanc ou
	 *         le noir)
	 */
	public Optional<Joueur> getGagnant() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return le joueur perdant s'il est connu (même instance que le blanc ou
	 *         le noir)
	 */
	public Optional<Joueur> getPerdant() {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @return l'identifiant du tournoi si la partie est liée à celui-ci
	 */
	public Optional<Integer> getIdTournoi() {
		throw new NotImplementedError();
	}

	@Override
	public String toString() {
		return "Partie [idPartie=" + getIdPartie() + ", date=" + getDate() + ", piecesRestantes=" + getPiecesRestantes() + ", blanc="
				+ getBlanc() + ", noir=" + getNoir() + ", gagnant=" + getGagnant() + ", perdant=" + getPerdant() + "]";
	}

	/**
	 * Crée une partie à l'issue inconnue à l'heure par défaut et l'enregistre
	 * en base de données.
	 * 
	 * @param blanc
	 * @param noir
	 * @param con
	 * @return La partie générée avec son identifiant correct
	 * @throws SQLException
	 */
	public static Partie create(Joueur blanc, Joueur noir, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Inscrit la partie dans un tournoi
	 * 
	 * @param t
	 */
	public void setTournoi(Tournoi t) {
		throw new NotImplementedError();
	}

	/**
	 * Charge une partie à partir de son identifiant
	 * 
	 * @param idPartie
	 * @param con
	 * @return la partie si elle existe
	 * @throws SQLException
	 */
	public static Optional<Partie> load(int idPartie, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Charge une liste de parties à partir de l'identifiant d'un tournoi
	 * 
	 * @param idTournoi
	 * @param con
	 * @return la liste de toutes les parties liées au tournoi donné
	 * @throws SQLException
	 */
	public static List<Partie> loadTournoi(int idTournoi, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @param con
	 * @return un classement des joueurs en fonction du nombre de parties
	 *         jouées. Un LinkedHashMap permet de lier le nombre de parties
	 *         jouées à chaque joueur tout en conservant le classement.
	 * @throws SQLException
	 */
	public static LinkedHashMap<Joueur, Integer> classementPartiesJouees(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * 
	 * @param con
	 * @return un classement des joueurs en fonction du nombre de parties
	 *         gagnées. Un LinkedHashMap permet de lier le nombre de parties
	 *         gagnées à chaque joueur tout en conservant le classement.
	 * @throws SQLException
	 */
	public static LinkedHashMap<Joueur, Integer> classementPartiesGagnees(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Sauvegarde les modifications d'une partie dans la base. On ne sauvegarde
	 * que la date de la partie et l'éventuel identifiant du tournoi. Pour
	 * indiquer le perdant ou le gagnant, utiliser {@link #setGagnant}.
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void save(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Indique le gagnant d'une partie à l'aide du booléen <tt>blancGagne</tt>,
	 * ainsi que le nombre de pièces restantes. Le résultat est enregistré dans
	 * la base et les scores ELO des deux joueurs sont mis à jour et
	 * sauvegardés.
	 * 
	 * @param blancGagne
	 * @param piecesRestantes
	 * @param con
	 * @throws SQLException
	 */
	public void setGagnant(boolean blancGagne, int piecesRestantes, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Partie) {
			return getIdPartie() == ((Partie) o).getIdPartie();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getIdPartie();
	}

}
