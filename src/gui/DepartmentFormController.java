package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {
	@FXML private TextField txtID;
	@FXML private TextField txtNAME;
	@FXML private Label errorLabel;
	@FXML private Button save;
	@FXML private Button cancel;
	
	@FXML public void onSave() {
		System.out.println("onSave");
	}
	
	@FXML public void onCancel() {
		System.out.println("onCancel");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextMaxLength(txtNAME, 30);
	}

}
