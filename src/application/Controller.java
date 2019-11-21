package application;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import bdd.CoSQL;
import bdd.DAOBdd;
import bdd.DAOTable;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import obj_affichage.Bdd;
import obj_affichage.Contenu;
import obj_affichage.Table;

public class Controller {
	private Boolean b = false;
	private Model m = new Model();
	// private ListView<Bdd> lvB = new ListView<>();

	public Controller() {
	}

	@FXML
	private ComboBox<String> combobox;

	@FXML
	private TextField ip, port, querryField;

	@FXML
	private Button co, exe, save, load, test;

	@FXML
	private TableView<Bdd> tabBdd;
	//
	@FXML
	private TableColumn<Bdd, String> colBdd;

	//
	@FXML
	private TableView<Table> tabTables;

	@FXML
	private TableColumn<Table, String> colTable;

	@FXML
	private TableView<Contenu> tabContenu;

	@FXML
	private Pane contenu_pane;

	@FXML
	private void initialize() {
		combobox.getItems().removeAll(combobox.getItems());
		combobox.getItems().addAll("MySQL", "Oracle", "MSSQL", "Derby", "");
		combobox.getSelectionModel().select("");
	}

	public void dealwithCombobox() {
		String str = combobox.getValue();

		switch (str) {
		default:
			break;
		case ("MySQL"):
			ip.setText("localhost");
			port.setText("3306");
			co.setDisable(false);
			b = true;
			break;
		case ("Oracle"):
			setdefault_field();
			co.setDisable(false);
			b = false;
			break;
		case ("MSSQL"):
			setdefault_field();
			co.setDisable(false);
			b = false;
			break;
		case ("Derby"):
			setdefault_field();
			co.setDisable(false);
			b = false;
			break;
		case (""):
			setdefault_field();
			co.setDisable(true);
			b = false;
			break;
		}
	}

	private void setdefault_field() {
		ip.setText("xxxx");
		port.setText("xxxx");
		ip.setEditable(false);
		port.setEditable(false);
	}

	public void connexion() {
		if (b == true) {
			// CoSQL.close();
			CoSQL.set_ip(ip.getText());
			CoSQL.set_port(port.getText());
			CoSQL.set_url();
			m.login();
			setup_bdd();
			exe.setDisable(false);
		} else {
			m.error_log();
		}
	}

	public void setup_bdd() {
		DAOBdd dbdd = new DAOBdd();
		try {
			colBdd = new TableColumn<Bdd, String>(CoSQL.getInstance().getMetaData().getDatabaseProductName());
		} catch (SQLException e) {
			System.out.println("oups, pas de nom :/");
		}

		tabBdd.setItems(dbdd.afficher());
		colBdd.setCellValueFactory(new PropertyValueFactory<Bdd, String>("_nom"));
		tabBdd.getColumns().clear();
		tabBdd.getColumns().add(colBdd);
		tabBdd.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		co.setDisable(true);
		combobox.setDisable(true);
		setup_table();
	}

	public void setup_table() {
		tabBdd.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String bdd = tabBdd.getSelectionModel().getSelectedItem().get_nom();

				DAOTable tbdd = new DAOTable(bdd);
				try {
					colTable = new TableColumn<Table, String>(
							CoSQL.getInstance().getMetaData().getDatabaseProductName());
				} catch (SQLException e) {
					System.out.println("oups, pas de nom :/");
				}

				tabTables.setItems(tbdd.afficher());
				colTable.setCellValueFactory(new PropertyValueFactory<Table, String>("_nom"));
				tabTables.getColumns().clear();
				tabTables.getColumns().add(colTable);
				tabTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				setup_contenu(bdd);

			}
		});
	}

	public void setup_contenu(String bdd) {
		tabTables.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String table = tabTables.getSelectionModel().getSelectedItem().get_nom();
				// ObservableList<Contenu> olc = FXCollections.observableArrayList();
				// ObservableList<String> ols = FXCollections.observableArrayList();
				//
				// try {
				// ResultSet result1 =
				// CoSQL.getInstance().createStatement().executeQuery("SELECT * FROM " + bdd +
				// "." + table);
				// ResultSetMetaData data = result1.getMetaData();
				// int nb_col = data.getColumnCount();
				// int i = 1;
				// int j = 1;
				// while(i <= nb_col) {
				// String colname = data.getColumnName(i);
				// i++;
				// }
				//
				// } catch (SQLException e) {
				// e.printStackTrace();
				// }
				// A TEST PLUS TARD, go swing

				ResultSet rs;
				try {
					rs = CoSQL.getInstance().createStatement().executeQuery("SELECT * FROM " + bdd + "." + table);
					JTable tabC = new JTable(Model.buildTableModel(rs));
					SwingNode sn = new SwingNode();
					contenu_pane.getChildren().clear();
					m.createAndSetSwingContent(sn, tabC);
					contenu_pane.getChildren().add(sn);

				} catch (SQLException e) {
					System.out.println("fail JTable");
					e.printStackTrace();
				}
			}
		});
	}

	public void clicExe() {
		String requette = querryField.getText().toUpperCase();

		try {
			PreparedStatement prepare = CoSQL.getInstance().prepareStatement(requette);

			if (requette.contains("SELECT")) {
				ResultSet rs = prepare.executeQuery();
				JTable tabC = new JTable(Model.buildTableModel(rs));
				SwingNode sn = new SwingNode();
				contenu_pane.getChildren().clear();
				m.createAndSetSwingContent(sn, tabC);
				contenu_pane.getChildren().add(sn);
				// exe.getStyleClass().removeAll("turnExeOrange, focus");
				// exe.getStyleClass().removeAll("turnExeRed, focus");
				// exe.getStyleClass().add("turnExeGreen");
			} else if (requette.contains("UPDATE") || requette.contains("INSERT") || requette.contains("DELETE")
					|| requette.contains("CREATE") || requette.contains("DROP")) {
				prepare.executeUpdate();
				querryField.setText("DONE : \"" + requette + "\"");
				// exe.getStyleClass().removeAll("turnExeOrange, focus");
				// exe.getStyleClass().removeAll("turnExeRed, focus");
				// exe.getStyleClass().add("turnExeGreen");
				tabBdd.getColumns().clear();
				setup_bdd();
			} else if (requette.equals("")) {
				System.out.println("formulez votre requete !");
				// exe.getStyleClass().removeAll("turnExeGreen, focus");
				// exe.getStyleClass().removeAll("turnExeRed, focus");
				// exe.getStyleClass().add("turnExeOrange");
			} else {
				System.out.println("requete non valide ou pas encore implémentée");
				// exe.getStyleClass().removeAll("turnExeRed, focus");
				// exe.getStyleClass().removeAll("turnExeGreen, focus");
				// exe.getStyleClass().add("turnExeOrange");
			}
		} catch (SQLException e) {
			System.out.println("requete valide mais peut être mal formulée mal formulée ?");
			// exe.getStyleClass().removeAll("turnExeOrange, focus");
			// exe.getStyleClass().removeAll("turnExeGreen, focus");
			// exe.getStyleClass().add("turnExeRed");

		}
	}

	public void save() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("amsql files (*.amsql)", "*.amsql");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			m.SaveFile(querryField.getText(), file);
		}

	}

	public void load() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilterAmsql = new FileChooser.ExtensionFilter("amsql files (*.amsql)",
				"*.amsql");
		fileChooser.getExtensionFilters().addAll(extFilterAmsql);
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			querryField.setText(m.LoadFile(file));
		}
	}

}
