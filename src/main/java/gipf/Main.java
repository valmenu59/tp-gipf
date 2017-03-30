package gipf;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.ds.common.BaseDataSource;

public class Main {
	private static final BaseDataSource DS;

	static {
		/** Configurer la source de données */
		DS = new PGSimpleDataSource();
		DS.setServerName("localhost");
		DS.setDatabaseName("gipf");
		DS.setUser("vion");
		DS.setPassword("vion");
	}

	/**
	 * Suppression de toutes les données
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public static void clean(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
			stmt.executeUpdate("TRUNCATE TABLE Partie, Arbitre, Tournoi, Joueur");
		}
	}

	public static Connection connect() throws SQLException {
		return DS.getConnection();
	}

}
