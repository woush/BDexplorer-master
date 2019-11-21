package application;

import java.io.IOException;
import java.util.Optional;

import bdd.CoSQL;
import bdd.DAOBdd;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

@SuppressWarnings("unused")
public class Main extends Application {
	private Stage primaryStage;
	private AnchorPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("BDD explorer"/* better use chromeBDD */);
		primaryStage.setOnCloseRequest(confirmCloseEventHandler);
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("Vue.fxml"));
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
		Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Vous quitter ce magnifique programme :'/ ?");
		Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("oui !");
		closeConfirmation.setHeaderText("QUITTER");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(primaryStage);

		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get())) {
			event.consume();
		}
	};

	public static void main(String[] args) {
		launch(args);
	}
}