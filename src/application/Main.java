package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// instancia o novo objeto "loader" do tipo FXMLLoader
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			// chama o load que carrega a view
			ScrollPane scrollPane = loader.load();
			
			//ajusta o menubar ao tamanho da tela
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			
			
			// cria o objeto do tipo Scene, que vai ser a cena principal
			// instancia a cena passando como argumento o objeto principal da view (que foi
			// declarado acima)
			// [ que nesse caso vai ser o AnchorPane vazio
			mainScene = new Scene(scrollPane);
			// faz o set da cena como sendo a mainScene declarada acima
			primaryStage.setScene(mainScene);
			// Define o título do palco
			primaryStage.setTitle("Sample JavaFX application");
			// exibe o palco
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
