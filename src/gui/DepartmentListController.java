package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
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
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	@FXML
	// ActionEvent event -> destinado a ter uma refer�ncia do controle que receber o
	// evento
	public void onBtNewAction(ActionEvent event) {
		// pega uma refer�ncia para o stage atual
		Stage parentStage = Utils.currentStage(event);
		// como � um bot�o para cadastrar um novo departamento, o formul�rio vai come�ar
		// vazio como abaixo
		Department obj = new Department();
		// passa essa refer�ncia para criar a nova janela
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
		// pega refer�ncia para o stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// acessa o servi�o, carrega os departamentos
	// e coloca os departamentos dentro da "ObservableList<Department> obsList"
	// ent�o ser� feita a associa��o do ObservableList com o TableView
	// e s� ent�o os departamentos aparecer�o na exibi��o
	public void updateTableView() {
		//
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// recupera os departamentos do servi�o (os que est�o no "mock")
		List<Department> list = service.findAll();
		// carregar a lista dentro do obsList
		obsList = FXCollections.observableArrayList(list);
		// carrega os itens na tableView para que sejam mostrados na tela
		tableViewDepartment.setItems(obsList);
		// acrescenta um bot�o com a palavra "edit" em cada linha da tabela
		initEditButtons();
		initRemoveButtons();
	}

	// no par�metro � uma refer�ncia ao stage que criou a janela de di�logo
	// para mostrar quem � o stage que criou a janela de di�logo
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			// carrega a visualiza��o de outra tela na frente da tela principal (arquivo
			// .fxml)
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carrega a view
			Pane pane = loader.load();

			// pega o controlador da tela que acabou de ser carregada acima
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			// define quem � o stage pai desta janela, neste caso o parentStage
			// por esse motivo foi necess�rio pegar essa informa��o por par�metro no in�cio
			// do m�todo
			dialogStage.initOwner(parentStage);
			// faz a janela ser modal
			// enquanto n�o fechar ela n�o consegue acessar a janela anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}

		}

	}

}
