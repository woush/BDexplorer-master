package obj_affichage;

import javafx.beans.property.SimpleStringProperty;

public class Bdd {
	private SimpleStringProperty _nom;

	public String get_nom() {
		return _nom.get();
	}

	public Bdd(String s) {
		_nom = new SimpleStringProperty(s);
	}
}
