package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.ouvintes.AtualizaDadosLista;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidacaoDeExcecao;
import model.service.ServicoDepartamento;

public class DepartamentoFormController implements Initializable{
	
	private Departamento entity;
	
	private ServicoDepartamento srvDepartamento;
	
	private List<AtualizaDadosLista> atualizaDadosLista = new ArrayList<>();
	
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
	
	public void sobrescreveAtualizaDadosLista (AtualizaDadosLista ouvintes) {
		atualizaDadosLista.add(ouvintes);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity está nulo");
		}
		if (srvDepartamento == null) {
			throw new IllegalStateException("srvDepartamento está nulo");
		}
		try {
			entity = getFormData();
			srvDepartamento.inserirOuAtualizarDepartamento(entity);
			notificaAtualizaDadosLista();
			Utils.currentStage(event).close();
		}
		catch (ValidacaoDeExcecao e) {
			setMensagemDeErros(e.getErros());
		}
		catch (DbException e) {
			Alertas.showAlert("Erro ao salvar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notificaAtualizaDadosLista() {
		for (AtualizaDadosLista x : atualizaDadosLista) {
			x.onAtualizaDados();
		}
	}

	private Departamento getFormData() {
		Departamento dep = new Departamento();
		
		ValidacaoDeExcecao excecao = new ValidacaoDeExcecao("Validação de erro!");
		
		dep.setId(Utils.tryParseToInt(txtId.getText())); // Verifica se o campo está preenchido com número inteiro
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) { // trim = Para eliminar qualquer espaço em branco no inicio ou no final.
			excecao.addErros("nome", " O campo não pode estar vazio!");
		}
		dep.setNome(txtNome.getText());
		
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
	}
	
	public void updateDados() {
		if (entity == null) {
			throw new IllegalStateException("Departamento nulo!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
	}
	
	private void setMensagemDeErros(Map<String, String> erros) { //Método para pegar os erros da exceção e anexar na tela
		Set<String> fields = erros.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(erros.get("nome"));
		}
	}
}
