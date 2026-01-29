package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
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
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {
	private SellerService sellServ;

	@FXML private TableView<Seller> tableViewSeller;
	@FXML private TableColumn<Seller, Integer> columnID;
	@FXML private TableColumn<Seller, String> columnNAME;
	@FXML private TableColumn<Seller, String> columnEMAIL;
	@FXML private TableColumn<Seller, Date> columnBIRTH;
	@FXML private TableColumn<Seller, Double> columnSALARY;
	@FXML private TableColumn<Seller, Seller> columnEDIT;
	@FXML private TableColumn<Seller, Seller> columnDELETE;

	@FXML private Button newSeller;
	@FXML private void onNewSeller(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller seller = new Seller();
		createDialogForm(seller, "/gui/SellerForm.fxml", parentStage);
	}

	private ObservableList<Seller> obsList;

	public void setSellerService(SellerService service) {
		this.sellServ = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnNAME.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnEMAIL.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		columnBIRTH.setCellValueFactory(new PropertyValueFactory<>("birth"));
		Utils.formatTableColumnDate(columnBIRTH, "dd/MM/yyyy");
		
		columnSALARY.setCellValueFactory(new PropertyValueFactory<>("salary"));
		Utils.formatTableColumnDouble(columnSALARY, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if(sellServ == null) {
			throw new IllegalStateException("Null service.");
		}

		List<Seller> list = sellServ.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller seller, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(seller);
			controller.setServices(sellServ, new DepartmentService());
			controller.loadDepartmentList();
			controller.subscribeDataUpdates(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter seller data: ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO exception", "Couldn't load view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}

	private void initEditButtons() {
		columnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		columnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(
								obj, "/gui/SellerForm.fxml",Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		columnDELETE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		columnDELETE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	private void removeEntity(Seller obj) {
		Optional<ButtonType> answer = Alerts.showConfirmation("Confirmation", "You're sure about this???");
		
		if(answer.get() == ButtonType.OK) {
			if(sellServ == null) {
				throw new IllegalStateException("Service can't be null.");
			}
			
			try {
				sellServ.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Couldn't remove seller.", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}
}
