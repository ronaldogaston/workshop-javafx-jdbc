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
import model.entities.Departamento;

public class ControllerListaDepartamento implements Initializable{
	
	@FXML
	private TableView<Departamento> tableViewDepartamento; // Tipo TableView
	
	@FXML
	private TableColumn<Departamento, Integer> colunaId; // Tipo Coluna. OBS: Lembrando que s� declarar o mesmo n�o faz com que funcione. Verifique o m�todo 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private TableColumn<Departamento, Integer> colunaNome; // Tipo Coluna. OBS: Lembrando que s� declarar o mesmo n�o faz com que funcione. Verifique o m�todo 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private Button btNew; // Tipo Bot�o
	
	@FXML
	public void onBtNewAction() { // A��o que ocorrer� ap�s o bot�o 'Novo' ser clicado
		System.out.println("onBtNewAction");
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

}
