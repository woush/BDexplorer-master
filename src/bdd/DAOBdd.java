package bdd;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import obj_affichage.Bdd;

public class DAOBdd extends DAOmanager<Bdd> {

	@Override
	public ObservableList<Bdd> afficher() {
		ObservableList<Bdd> olb = FXCollections.observableArrayList();

		try {
			ResultSet result = this.connect.createStatement().executeQuery("SHOW DATABASES");
			while (result.next()) {
				Bdd b = new Bdd(result.getString(1));
				olb.add(b);
			}
		} catch (SQLException e) {
			System.out.println("fail affichage");
		}
		return olb;
	}
}
