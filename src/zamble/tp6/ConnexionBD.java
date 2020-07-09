package leveque.zamble.tp6;

import java.sql.*;
import java.util.Map;

public class ConnexionBD {

	private static Connection connexion;

	public ConnexionBD() {
		try {
			connexion = connecterBD();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection connecterBD() throws SQLException {
		//connexion = DriverManager.getConnection("jdbc:mysql://mysql.istic.univ-rennes1.fr/base_bousse", "user_jzamble", "12345678");
		connexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1/DB_MotsCroisés?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		return connexion;
	}

	 public void close() throws SQLException {
	        connexion.close();
	 }

	 public int RandomGrilleNum() {
		 int numberGrid = 1;
		 try {
			connecterBD();
			PreparedStatement pS = connexion.prepareStatement("SELECT num_grille FROM TP5_GRILLE ORDER BY RAND() LIMIT 1");
			ResultSet resultSet = pS.executeQuery();
			while (resultSet.next()) {
				numberGrid = resultSet.getInt("num_grille");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return numberGrid;
	 }

	/*
	@SuppressWarnings("null")
	public Map<Integer, String> grillesDisponibles() {
		Map<Integer, String> map = null;
		String select = "Select num_grille, nom_grille, largeur, hauteur FROM base_bousse.TP5_Grille";
		try {
			PreparedStatement preparedStatement = connexion.prepareStatement(select);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				int num = resultSet.getInt("num_grille");
				String nom = resultSet.getString("nom_grille");
				int h= resultSet.getInt("hauteur");
				int l = resultSet.getInt("largeur");

				nom += "("+h+"x"+l+")";
				map.put(num, nom);

			}
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	*/

}
