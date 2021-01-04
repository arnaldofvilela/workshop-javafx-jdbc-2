 package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{

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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
		
		//faz com que a tableView acompanhe o tamanho da janela
		//pega referência para o stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	//acessa o serviço, carrega os departamentos 
	// e coloca os departamentos dentro da "ObservableList<Department> obsList"
	// então será feita a associação do ObservableList com o TableView
	// e só então os departamentos aparecerão na exibição
	public void updateTableView() {
		//
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		//recupera os departamentos do serviço (os que estão no "mock")
		List<Department> list = service.findAll();
		//carregar a lista dentro do obsList
		obsList = FXCollections.observableArrayList(list);
		//carrega os itens na tableView para que sejam mostrados na tela
		tableViewDepartment.setItems(obsList);
		
	}
}
