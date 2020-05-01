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
import model.entities.Departamento;
import model.service.ServicoDepartamento;

public class ControllerListaDepartamento implements Initializable{
	
	private ServicoDepartamento service; // Injetar a depe�ncia sem colocar a implementa��o 'new ServicoDepartamento'
	
	@FXML
	private TableView<Departamento> tableViewDepartamento; // Tipo TableView
	
	@FXML
	private TableColumn<Departamento, Integer> colunaId; // Tipo Coluna. OBS: Lembrando que s� declarar o mesmo n�o faz com que funcione. Verifique o m�todo 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private TableColumn<Departamento, Integer> colunaNome; // Tipo Coluna. OBS: Lembrando que s� declarar o mesmo n�o faz com que funcione. Verifique o m�todo 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private Button btNew; // Tipo Bot�o
	
	private ObservableList<Departamento> obsList;
	
	@FXML
	public void onBtNewAction() { // A��o que ocorrer� ap�s o bot�o 'Novo' ser clicado
		System.out.println("onBtNewAction");
	}
	
	public void setServicoDepartamento(ServicoDepartamento service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		InitializeNodes(); // Macete para funcionar um componente da tela. Veja o m�todo 'InitializeNodes()'
	}

	private void InitializeNodes() {
		colunaId.setCellValueFactory(new PropertyValueFactory<>("id")); // Comando para iniciar apropriadamento o comportamento da coluna na tabela
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome")); // Comando para iniciar apropriadamento o comportamento da coluna na tabela
		
		/*
		 * Comando para a tabela acompanhar a altura da janela
		 * ABAIXO 
		 */
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Servi�o est� nulo.");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}

}
