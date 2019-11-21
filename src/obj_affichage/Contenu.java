package obj_affichage;

import javafx.beans.property.SimpleStringProperty;

public class Contenu {

	private SimpleStringProperty _nom;
	private SimpleStringProperty _table;

	public String get_nom() {
		return _nom.get();
	}

	public String get_bdd() {
		return _table.get();
	}

	public Contenu(String s, String t) {
		_nom = new SimpleStringProperty(s);
		_table = new SimpleStringProperty(t);
	}
}
