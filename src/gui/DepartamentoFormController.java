package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.service.ServicoDepartamento;

public class DepartamentoFormController implements Initializable{
	
	private Departamento entity;
	
	private ServicoDepartamento srvDepartamento;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setDepartamento (Departamento entity) {
		this.entity = entity;
	}
	
	public void setServicoDepartamento (ServicoDepartamento srvDepartamento) {
		this.srvDepartamento = srvDepartamento;
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity est� nulo");
		}
		if (srvDepartamento == null) {
			throw new IllegalStateException("srvDepartamento est� nulo");
		}
		try {
			entity = getFormData();
			srvDepartamento.inserirOuAtualizarDepartamento(entity);
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alertas.showAlert("Erro ao salvar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getFormData() {
		Departamento dep = new Departamento();
		
		dep.setId(Utils.tryParseToInt(txtId.getText())); // Verifica se o campo est� preenchido com n�mero inteiro
		dep.setNome(txtNome.getText());
		
		return dep;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateDados() {
		if (entity == null) {
			throw new IllegalStateException("Departamento nulo!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
	}
}
