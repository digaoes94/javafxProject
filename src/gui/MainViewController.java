package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {
	@FXML private MenuItem miSeller;
	@FXML private MenuItem miDepartment;
	@FXML private MenuItem miAbout;

	@FXML public void onMiSellerAction() {
		System.out.println("onMiSellerAction");
	}
	
	@FXML public void onMiDepartmentAction() {
		System.out.println("onMiDepartmentAction");
	}
	
	@FXML public void onAboutAction() {
		System.out.println("onAboutAction");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

}
