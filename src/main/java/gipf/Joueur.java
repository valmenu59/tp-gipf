package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe représente un joueur, avec son login, email, mot de passe et
 * score ELO. Les méthodes {@link #equals} et {@link #hashCode} sont redéfinies
 * pour que deux joueurs soient considérés comme égaux s'ils ont le même login.
 */
public class Joueur {
	/**
	 * Construit un joueur avec tous ses attributs
	 * 
	 * @param login
	 * @param email
	 * @param password
	 * @param elo
	 */
	public Joueur(String login, String email, String password, double elo) {
		super();
		throw new NotImplementedError();
	}

	/**
	 * @return le score ELO du joueur
	 */
	public double getElo() {
		throw new NotImplementedError();
	}

	/**
	 * Incrémente le score ELO du joueur (l'argument peut être négatif)
	 * 
	 * @param elo
	 */
	public void addElo(double elo) {
		throw new NotImplementedError();
	}

	/**
	 * @return le login du joueur
	 */
	public String getLogin() {
		throw new NotImplementedError();
	}

	/**
	 * Modifie le mot de passe du joueur
	 * 
	 * @param s
	 */
	public void setPassword(String s) {
		throw new NotImplementedError();
	}

	/**
	 * Modifie l'adresse mail du joueur
	 * 
	 * @param s
	 */
	public void setEmail(String s) {
		throw new NotImplementedError();
	}

	@Override
	public String toString() {
		return String.format("Joueur [login=%s, elo=%.0f]", getLogin(), getElo());
	}

	/**
	 * Sauvegarde un joueur (ELO, mot de passe et email)
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void save(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * Inscrit un joueur et l'enregistre en base de données Le score ELO est
	 * attribué à sa valeur par défaut
	 * 
	 * @param login
	 * @param password
	 * @param email
	 * @param con
	 * @return le joueur inscrit
	 * @throws SQLException
	 * @throws InscriptionException
	 *             si le login ou l'email existent déjà, si l'email ne comporte
	 *             pas le caractère '@' ou une autre contrainte d'intégrité n'a
	 *             pas été respectée
	 */
	public static Joueur inscrire(String login, String password, String email, Connection con)
			throws SQLException, InscriptionException {
		throw new NotImplementedError();
	}

	/**
	 * Charge un joueur de la base à partir du login donné
	 * 
	 * @param login
	 * @param con
	 * @return un Optional contenant le joueur s'il existe
	 * @throws SQLException
	 */
	public static Optional<Joueur> load(String login, Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * @param con
	 * @return la liste des joueurs de la base de données, classés par ELO
	 * @throws SQLException
	 */
	public static List<Joueur> loadByElo(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * @return l'email du joueur
	 */
	public String getEmail() {
		throw new NotImplementedError();
	}

	/**
	 * Contrôle la validité d'un mot de passe
	 * 
	 * @param p
	 * @return <tt>true</tt> si et seulement si <tt>p</tt> est le bon mot de
	 *         passe
	 */
	public boolean checkPassword(String p) {
		throw new NotImplementedError();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Joueur) {
			return getLogin().equals(((Joueur) o).getLogin());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getLogin().hashCode();
	}

}
