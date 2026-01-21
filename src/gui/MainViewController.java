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

public class MainViewController implements Initializable {
	@FXML private MenuItem miSeller;
	@FXML private MenuItem miDepartment;
	@FXML private MenuItem miAbout;

	@FXML public void onMiSellerAction() {
		System.out.println("onMiSellerAction");
	}
	
	@FXML public void onMiDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", 
				(DepartmentListController controller) -> {controller.setDepartmentService(new DepartmentService()); controller.updateTableView();}
		);
	}
	
	@FXML public void onAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(vbox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException e) {
			Alerts.showAlert("I/O exception", "Could not load view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
