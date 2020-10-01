package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.ouvintes.AtualizaDadosLista;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidacaoDeExcecao;
import model.service.ServicoDepartamento;
import model.service.ServicoVendedor;

public class VendedorFormController implements Initializable {

	private Vendedor entity;

	private ServicoVendedor srvVendedor;

	private ServicoDepartamento srvDepartamento;

	private List<AtualizaDadosLista> atualizaDadosLista = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorDataNascimento;

	@FXML
	private Label labelErrorSalarioBase;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	private ObservableList<Departamento> obsList;

	public void setVendedor(Vendedor entity) {
		this.entity = entity;
	}

	public void setServicos(ServicoVendedor srvVendedor, ServicoDepartamento srvDepartamento) {
		this.srvVendedor = srvVendedor;
		this.srvDepartamento = srvDepartamento;
	}

	public void sobrescreveAtualizaDadosLista(AtualizaDadosLista ouvintes) {
		atualizaDadosLista.add(ouvintes);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity está nulo");
		}
		if (srvVendedor == null) {
			throw new IllegalStateException("srvVendedor está nulo");
		}
		try {
			entity = getFormData();
			srvVendedor.inserirOuAtualizarVendedor(entity);
			notificaAtualizaDadosLista();
			Utils.currentStage(event).close();
		} catch (ValidacaoDeExcecao e) {
			setMensagemDeErros(e.getErros());
		} catch (DbException e) {
			Alertas.showAlert("Erro ao salvar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificaAtualizaDadosLista() {
		for (AtualizaDadosLista x : atualizaDadosLista) {
			x.onAtualizaDados();
		}
	}

	private Vendedor getFormData() {
		Vendedor dep = new Vendedor();

		ValidacaoDeExcecao excecao = new ValidacaoDeExcecao("Validação de erro!");

		dep.setId(Utils.tryParseToInt(txtId.getText())); // Verifica se o campo está preenchido com número inteiro
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) { // trim = Para eliminar qualquer espaço	// em branco no inicio ou no final.
			excecao.addErros("nome", " O campo não pode estar vazio!");
		}
		dep.setNome(txtNome.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) { // trim = Para eliminar qualquer espaço
			// em branco no inicio ou no final.
			excecao.addErros("email", " O campo não pode estar vazio!");
		}
		dep.setEmail(txtEmail.getText());
		
		if (dpDataNascimento.getValue() == null) {
			excecao.addErros("dataNascimento", " O campo não pode estar vazio!");
		}
		else {
		Instant instant = Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
		dep.setDataNascimento(Date.from(instant));
		}
		
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) { // trim = Para eliminar qualquer espaço em branco no inicio ou no final.
			excecao.addErros("salarioBase", " O campo não pode estar vazio!");
		}
		dep.setSalarioBase(Utils.tryParseToDouble(txtSalarioBase.getText()));
		
		dep.setDepartamento(comboBoxDepartamento.getValue());
		
		if (excecao.getErros().size() > 0) { // Teste na coleção de erros, se há algum erro.
			throw excecao;
		}

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
		Restricoes.setTextFieldDouble(txtSalarioBase);
		Restricoes.setTextFieldMaxLength(txtEmail, 40);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");

		initializeComboBoxDepartamento();
	}

	public void updateDados() {
		if (entity == null) {
			throw new IllegalStateException("Vendedor nulo!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		if (entity.getDataNascimento() != null) {
			dpDataNascimento
					.setValue(LocalDate.ofInstant(entity.getDataNascimento().toInstant(), ZoneId.systemDefault())); // Comando
																													// Zone
																													// para
																													// pegar
																													// horário
																													// da
																													// máquina
																													// do
																													// usuário
		}
		if (entity.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartamento.setValue(entity.getDepartamento());
		}
		txtSalarioBase.setText(String.format("%.2f", entity.getSalarioBase()));
	}

	public void carregarObjAssociados() {
		if (srvDepartamento == null) {
			throw new IllegalStateException("Serviço de Departamento é nulo"); // Programação defensiva - Injeção de
																				// Dependência faltando
		}
		List<Departamento> list = srvDepartamento.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);
	}

	private void setMensagemDeErros(Map<String, String> erros) { // Método para pegar os erros da exceção e anexar na
																	// tela
		Set<String> fields = erros.keySet();
		
		labelErrorNome.setText((fields.contains("nome") ? erros.get("nome") : ""));
		labelErrorEmail.setText((fields.contains("email") ? erros.get("email") : ""));
		labelErrorDataNascimento.setText((fields.contains("dataNascimento") ? erros.get("dataNascimento") : ""));
		labelErrorSalarioBase.setText((fields.contains("salarioBase") ? erros.get("salarioBase") : ""));

	}

	private void initializeComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}
}
