package bdd;

import java.sql.Connection;

import javafx.collections.ObservableList;

public abstract class DAOmanager<T> {
	protected Connection connect = CoSQL.getInstance();

	public abstract ObservableList<T> afficher();
}
