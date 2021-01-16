package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	@FXML
	//ActionEvent event -> destinado a ter uma referência do controle que receber o evento
	public void onBtNewAction(ActionEvent event) {
		//pega uma referência para o stage atual
		Stage parentStage = Utils.currentStage(event);
		//como é um botão para cadastrar um novo departamento, o formulário vai começar vazio como abaixo
		Department obj = new Department();
		//passa essa referência para criar a nova janela
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// faz com que a tableView acompanhe o tamanho da janela
		// pega referência para o stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// acessa o serviço, carrega os departamentos
	// e coloca os departamentos dentro da "ObservableList<Department> obsList"
	// então será feita a associação do ObservableList com o TableView
	// e só então os departamentos aparecerão na exibição
	public void updateTableView() {
		//
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// recupera os departamentos do serviço (os que estão no "mock")
		List<Department> list = service.findAll();
		// carregar a lista dentro do obsList
		obsList = FXCollections.observableArrayList(list);
		// carrega os itens na tableView para que sejam mostrados na tela
		tableViewDepartment.setItems(obsList);

	}

	// no parâmetro é uma referência ao stage que criou a janela de diálogo
	// para mostrar quem é o stage que criou a janela de diálogo
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			// carrega a visualização de outra tela na frente da tela principal (arquivo
			// .fxml)
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carrega a view
			Pane pane = loader.load();

			//pega o controlador da tela que acabou de ser carregada acima
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			// define quem é o stage pai desta janela, neste caso o parentStage
			// por esse motivo foi necessário pegar essa informação por parâmetro no início
			// do método
			dialogStage.initOwner(parentStage);
			// faz a janela ser modal
			// enquanto não fechar ela não consegue acessar a janela anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} 
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
