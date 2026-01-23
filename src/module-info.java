module javafxProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	
	opens gui to javafx.fxml;
	exports gui;
	
	opens gui.util to javafx.fxml;
	exports gui.util;
	
	opens model.entities to javafx.fxml;
	exports model.entities;
	
	opens model.services to javafx.fxml;
	exports model.services;
	
	opens model.dao.impl to javafx.fxml;
	exports model.dao.impl;
	
	opens model.dao to javafx.fxml;
	exports model.dao;
	
	opens db to javafx.fxml;
	exports db;
}
