package gipf;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Une classe permettant d'initialiser la connection à la base de données et
 * faire quelques tests si nécessaire
 */
public class Main {
	/**
	 * Construction désactivée
	 */
	private Main() {
		throw new IllegalStateException();
	}

	/**
	 * Supprime toutes les données de la base
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public static void clean(Connection con) throws SQLException {
		throw new NotImplementedError();
	}

	/**
	 * @return une connection à la base de données
	 * @throws SQLException
	 */
	public static Connection connect() throws SQLException {
		throw new NotImplementedError();
	}

}
