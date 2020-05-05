package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.ouvintes.AtualizaDadosLista;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.service.ServicoDepartamento;

public class ControllerListaDepartamento implements Initializable, AtualizaDadosLista{
	
	private ServicoDepartamento service; // Injetar a depeência sem colocar a implementação 'new ServicoDepartamento'
	
	@FXML
	private TableView<Departamento> tableViewDepartamento; // Tipo TableView
	
	@FXML
	private TableColumn<Departamento, Integer> colunaId; // Tipo Coluna. OBS: Lembrando que só declarar o mesmo não faz com que funcione. Verifique o método 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private TableColumn<Departamento, Integer> colunaNome; // Tipo Coluna. OBS: Lembrando que só declarar o mesmo não faz com que funcione. Verifique o método 'initialize (URL uri, ResourceBundle rb)'
	
	@FXML
	private Button btNew; // Tipo Botão
	
	private ObservableList<Departamento> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) { // Ação que ocorrerá após o botão 'Novo' ser clicado -- (ActionEvent event) Para ter referencia do controle que receber o evento
		Stage parentStage = Utils.currentStage(event);
		Departamento dep = new Departamento();
		cadastroDialogoFormulario(dep, "/gui/DepartamentoForm.fxml", parentStage); // Chamada do método de formulário de cadastro
	}
	
	public void setServicoDepartamento(ServicoDepartamento service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		InitializeNodes(); // Macete para funcionar um componente da tela. Veja o método 'InitializeNodes()'
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
			throw new IllegalStateException("Serviço está nulo.");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}

	private void cadastroDialogoFormulario(Departamento dep, String nomeAbsoluto, Stage parentStage) { // Janela de Diálogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto)); // Padrão do método ' (getClass().getResource(nomeAbsoluto)) '
			Pane pane = loader.load();
			
			DepartamentoFormController controller = loader.getController();
			controller.setDepartamento(dep);
			controller.setServicoDepartamento(new ServicoDepartamento());
			controller.sobrescreveAtualizaDadosLista(this); // Cadeia de chamadas até o método 'onAtualizaDados'
			controller.updateDados();
			
			Stage dialogoStage = new Stage();
			
			dialogoStage.setTitle("Informe os dados do departamento");
			dialogoStage.setScene(new Scene(pane)); // Chamada de nova janela (janela filho) que irá sobrepor a anterior
			dialogoStage.setResizable(false); // Faz com que a tela NÃO possa ser máximizada/minimizada (Redimencionada)
			dialogoStage.initOwner(parentStage); // Chamada da janela pai da janela filho
			dialogoStage.initModality(Modality.WINDOW_MODAL); // Bloqueia o acesso as telas de fundos até que janela filho tenha sido finalizada com sucesso
			dialogoStage.showAndWait(); 
			
			/*
			 * Função para chamar a Janela do formulário de dialogo
			 * Para preencher o novo departamento
			 */
		} 
		catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro ao carrega janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onAtualizaDados() {
		updateTableView();
	}
	
}
