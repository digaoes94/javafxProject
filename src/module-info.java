module javafxProject {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
