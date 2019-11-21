package obj_affichage;

import javafx.beans.property.SimpleStringProperty;

public class Table {
	private SimpleStringProperty _nom;
	private SimpleStringProperty _bdd;

	public String get_nom() {
		return _nom.get();
	}

	public String get_bdd() {
		return _bdd.get();
	}

	public Table(String s, String b) {
		_nom = new SimpleStringProperty(s);
		_bdd = new SimpleStringProperty(b);
	}
}
