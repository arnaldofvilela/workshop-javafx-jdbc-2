package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	//Objetivo deste m�todo � carregar a outra view(tela) dentro da janela principal j� existente
	//para que possa trabalhar com a janela principal � preciso pegar uma refer�ncia da "cena" 
	//...a "cena" foi declarada dentro do Main.java da seguinte forma: private static Scene mainScene;
	//
	private synchronized void loadView(String absoluteName) {
		try {
			
			// carrega a visualiza��o de outra tela na frente da tela principal (arquivo .fxml)
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			// Vari�vel mainScene recebendo o valor da tela que est� instanciada no momento
			Scene mainScene = Main.getMainScene();
			// pegando refer�ncia ao VBox da janela principal
			// mainScene.getRoot() : pega o primeiro elemento do MainView.fxml (que � o
			// ScrollPane)
			// getContent pega o conta�do dentro do ScrollPane (neste caso � a VBox)
			// Desta forma pega-se uma refer�ncia para o VBox que est� na janela principal
			// (MainView.fxml)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// primeiro filho do VBox da janela principal (que � o MenuBar do MainView.fxml)
			Node mainMenu = mainVBox.getChildren().get(0);
			// limpa todos os filhos do VBox do MainView.fxml
			mainVBox.getChildren().clear();
			//adiciona o mainMenu
			mainVBox.getChildren().add(mainMenu);
			//adiciona os filhos da janela que for abrir
			mainVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
