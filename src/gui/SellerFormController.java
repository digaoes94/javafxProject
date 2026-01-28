package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	private Seller seller;
	private SellerService service;
	private List<DataChangeListener> observedDataUpdates = new ArrayList<DataChangeListener>();
	
	@FXML private TextField txtID;
	
	@FXML private TextField txtNAME;
	@FXML private Label errorLabel1;
	
	@FXML private TextField txtEMAIL;
	@FXML private Label errorLabel2;
	
	@FXML private DatePicker txtBIRTH;
	@FXML private Label errorLabel3;
	
	@FXML private TextField txtSALARY;
	@FXML private Label errorLabel4;
	
	@FXML private Button save;
	@FXML private Button cancel;
	
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataUpdates(DataChangeListener listener) {
		observedDataUpdates.add(listener);
	}
	
	@FXML public void onSave(ActionEvent event) {
		if(seller == null) {
			throw new IllegalStateException("No Seller instance avaible.");
		}
		
		if(service == null) {
			throw new IllegalStateException("No Seller Service instance avaible.");
		}
		
		try {
			seller = getFormData();
			service.saveOrUpdate(seller);
			informDataUpdate();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving seller.", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void informDataUpdate() {
		for(DataChangeListener listener : observedDataUpdates) {
			listener.onDataChange();
		}
		
	}

	@FXML public void onCancel(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextMaxLength(txtNAME, 70);
		Constraints.setTextMaxLength(txtEMAIL, 40);
		Utils.formatDatePicker(txtBIRTH, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtSALARY);
	}
	
	private Seller getFormData() {
		ValidationException errors = new ValidationException("Validation error");
		
		if(txtNAME.getText() == null || txtNAME.getText().trim().equals("")) {
			errors.addError("Name", "Name can't be null.");
		}
		
		if(errors.getErrors().size() > 0) {
			throw errors;
		}
		
		return new Seller(Utils.stringToInteger(txtID.getId()), txtNAME.getText());
	}
	
	public void updateFormData() {
		if(seller == null) {
			throw new IllegalStateException("Null object.");
		}
		
		txtID.setText(String.valueOf(seller.getId()));
		txtNAME.setText(seller.getName());
		txtEMAIL.setText(seller.getEmail());
		
		if(seller.getBirth() != null) {
			txtBIRTH.setValue(LocalDate.ofInstant(seller.getBirth().toInstant(), ZoneId.systemDefault()));
		}
		
		Locale.setDefault(Locale.US);
		txtSALARY.setText(String.format("%.2f", seller.getSalary()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if(fields.contains("Name")) {
			errorLabel1.setText(errors.get("Name"));
		}
	}
}
