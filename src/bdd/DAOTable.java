package bdd;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import obj_affichage.Table;

public class DAOTable extends DAOmanager<Table> {
	private String bdd;

	public DAOTable(String s) {
		bdd = s;
	}

	@Override
	public ObservableList<Table> afficher() {
		if (bdd != null && !bdd.equals("")) {
			ObservableList<Table> olb = FXCollections.observableArrayList();

			try {
				ResultSet result = this.connect.createStatement().executeQuery("SHOW tables FROM " + bdd);
				while (result.next()) {
					Table t = new Table(result.getString(1), bdd);
					olb.add(t);
				}
			} catch (SQLException e) {
				System.out.println("fail affichage");
			}
			return olb;
		} else
			return null;
	}
}
