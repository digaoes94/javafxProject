package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {
	@FXML private TableView<Department> tableViewDepartment;
	@FXML private TableColumn<Department, Integer> columnID;
	@FXML private TableColumn<Department, String> columnNAME;
	
	@FXML private Button newDepartment;
	@FXML private void onNewDepartment() {
		System.out.println("onNewDepartment");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		columnNAME.setCellValueFactory(new PropertyValueFactory<>("NAME"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
