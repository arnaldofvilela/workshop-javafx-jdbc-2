package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", 
				(SellerListController controller) -> 
					{
						controller.setSellerService(new SellerService());
						controller.updateTableView();
					}
			);
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", 
			(DepartmentListController controller) -> 
				{
					controller.setDepartmentService(new DepartmentService());
					controller.updateTableView();
				}
		);
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	// Objetivo deste método é carregar a outra view(tela) dentro da janela
	// principal já existente
	// para que possa trabalhar com a janela principal é preciso pegar uma
	// referência da "cena"
	// ...a "cena" foi declarada dentro do Main.java da seguinte forma: private
	// static Scene mainScene;
	//
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {

			// carrega a visualização de outra tela na frente da tela principal (arquivo
			// .fxml)
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			// Variável mainScene recebendo o valor da tela que está instanciada no momento
			Scene mainScene = Main.getMainScene();
			// pegando referência ao VBox da janela principal
			// mainScene.getRoot() : pega o primeiro elemento do MainView.fxml (que é o
			// ScrollPane)
			// getContent pega o contaúdo dentro do ScrollPane (neste caso é a VBox)
			// Desta forma pega-se uma referência para o VBox que está na janela principal
			// (MainView.fxml)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// primeiro filho do VBox da janela principal (que é o MenuBar do MainView.fxml)
			Node mainMenu = mainVBox.getChildren().get(0);
			// limpa todos os filhos do VBox do MainView.fxml
			mainVBox.getChildren().clear();
			// adiciona o mainMenu
			mainVBox.getChildren().add(mainMenu);
			// adiciona os filhos da janela que for abrir
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			
			//o getController vai retornar um controlador do tipo que for chamado acima 
			//por exemplo DepartmentListController
			T controller = loader.getController();
			initializingAction.accept(controller);
			
			
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
