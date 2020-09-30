package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.ouvintes.AtualizaDadosLista;
import gui.util.Alertas;
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
import model.entities.Vendedor;
import model.service.ServicoDepartamento;
import model.service.ServicoVendedor;

public class ControllerListaVendedor implements Initializable, AtualizaDadosLista {

	private ServicoVendedor service; // Injetar a depeência sem colocar a implementação 'new ServicoVendedor'

	@FXML
	private TableView<Vendedor> tableViewVendedor; // Tipo TableView

	@FXML
	private TableColumn<Vendedor, Integer> colunaId; // Tipo Coluna. OBS: Lembrando que só declarar o mesmo não faz
															// com que funcione. Verifique o método 'initialize (URL
															// uri, ResourceBundle rb)'

	@FXML
	private TableColumn<Vendedor, String> colunaNome; // Tipo Coluna. OBS: Lembrando que só declarar o mesmo não
															// faz com que funcione. Verifique o método 'initialize (URL
															// uri, ResourceBundle rb)'

	@FXML
	private TableColumn<Vendedor, String> colunaEmail;
	
	@FXML
	private TableColumn<Vendedor, Date> colunaDataNascimento;
	
	@FXML
	private TableColumn<Vendedor, Double> colunaSalarioBase;
	
	@FXML
	private TableColumn<Vendedor, Vendedor> colunaEditar; // Tipo Coluna. OBS: Lembrando que só declarar o mesmo
																	// não faz com que funcione. Verifique o método
																	// 'initialize (URL uri, ResourceBundle rb)'

	@FXML
	private TableColumn<Vendedor, Vendedor> colunaRemove;

	@FXML
	private Button btNew; // Tipo Botão

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) { // Ação que ocorrerá após o botão 'Novo' ser clicado -- (ActionEvent
													// event) Para ter referencia do controle que receber o evento
		Stage parentStage = Utils.currentStage(event);
		Vendedor dep = new Vendedor();
		cadastroDialogoFormulario(dep, "/gui/VendedorForm.fxml", parentStage); // Chamada do método de formulário de
																					// cadastro
	}

	public void setServicoVendedor(ServicoVendedor service) {
		this.service = service;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		InitializeNodes(); // Macete para funcionar um componente da tela. Veja o método
							// 'InitializeNodes()'
	}

	private void InitializeNodes() {
		colunaId.setCellValueFactory(new PropertyValueFactory<>("id")); // Comando para iniciar apropriadamento o
																		// comportamento da coluna na tabela
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome")); // Comando para iniciar apropriadamento o
																			// comportamento da coluna na tabela
		colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatTableColumnDate(colunaDataNascimento, "dd/MM/yyyy"); // Necessário criar o método para formatar a data
		colunaSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(colunaSalarioBase, 2); // Necessário criar o método para formatar o valor do salário, devido a 'vírgula'
		/*
		 * Comando para a tabela acompanhar a altura da janela ABAIXO
		 */

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço está nulo.");
		}
		List<Vendedor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsList);
		inicBotaoEditar();
		inicBotaoRemove();
	}

	private void cadastroDialogoFormulario(Vendedor dep, String nomeAbsoluto, Stage parentStage) { // Janela de
																										// Diálogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto)); // Padrão do método '
																						// (getClass().getResource(nomeAbsoluto))
																						// '
			Pane pane = loader.load();

			VendedorFormController controller = loader.getController();
			controller.setVendedor(dep);
			controller.setServicos(new ServicoVendedor(), new ServicoDepartamento());
			controller.carregarObjAssociados();
			controller.sobrescreveAtualizaDadosLista(this); // Cadeia de chamadas até o método 'onAtualizaDados'
			controller.updateDados();

			Stage dialogoStage = new Stage();

			dialogoStage.setTitle("Informe os dados do departamento");
			dialogoStage.setScene(new Scene(pane)); // Chamada de nova janela (janela filho) que irá sobrepor a anterior
			dialogoStage.setResizable(false); // Faz com que a tela NÃO possa ser máximizada/minimizada (Redimencionada)
			dialogoStage.initOwner(parentStage); // Chamada da janela pai da janela filho
			dialogoStage.initModality(Modality.WINDOW_MODAL); // Bloqueia o acesso as telas de fundos até que janela
																// filho tenha sido finalizada com sucesso
			dialogoStage.showAndWait();

			/*
			 * Função para chamar a Janela do formulário de dialogo Para preencher o novo
			 * departamento
			 */
		} catch (IOException e) {
			e.printStackTrace();
			Alertas.showAlert("IO Exception", "Erro ao carrega janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onAtualizaDados() {
		updateTableView();
	}

	private void inicBotaoEditar() {
		colunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> cadastroDialogoFormulario(obj, "/gui/VendedorForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

	private void inicBotaoRemove() {
		colunaRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaRemove.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
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

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> result = Alertas.showConfirmation("Confirmação", "Tem certeza que deseja excluir ?");
		//Optional é o objeto que carrega outro objeto podendo estar presente ou não.
		if (result.get() == ButtonType.OK) { // result.get = Para poder acessar esse outro objeto.
			if (service == null) { //Teste se o programador esqueceu de 'injetar' a dependencia
				throw new IllegalStateException("Serviço está nulo");
			}
			try {
				service.remove(obj);
				updateTableView(); // Força atualização dos dados da tabela.
			}
			catch (DbIntegrityException e) {
				Alertas.showAlert("Erro ao tentar remover !", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
