package leveque.zamble.tp6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTP6 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try
		{
	        primaryStage.setTitle("TP6 Leveque-Zamble");
			Parent root = FXMLLoader.load(getClass().getResource("MenuTp6.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
