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
import model.entities.Vendedor;
import model.exceptions.ValidacaoDeExcecao;
import model.service.ServicoVendedor;

public class VendedorFormController implements Initializable{
	
	private Vendedor entity;
	
	private ServicoVendedor srvVendedor;
	
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
	
	public void setVendedor (Vendedor entity) {
		this.entity = entity;
	}
	
	public void setServicoVendedor (ServicoVendedor srvVendedor) {
		this.srvVendedor = srvVendedor;
	}
	
	public void sobrescreveAtualizaDadosLista (AtualizaDadosLista ouvintes) {
		atualizaDadosLista.add(ouvintes);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity est� nulo");
		}
		if (srvVendedor == null) {
			throw new IllegalStateException("srvVendedor est� nulo");
		}
		try {
			entity = getFormData();
			srvVendedor.inserirOuAtualizarVendedor(entity);
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

	private Vendedor getFormData() {
		Vendedor dep = new Vendedor();
		
		ValidacaoDeExcecao excecao = new ValidacaoDeExcecao("Valida��o de erro!");
		
		dep.setId(Utils.tryParseToInt(txtId.getText())); // Verifica se o campo est� preenchido com n�mero inteiro
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) { // trim = Para eliminar qualquer espa�o em branco no inicio ou no final.
			excecao.addErros("nome", " O campo n�o pode estar vazio!");
		}
		dep.setNome(txtNome.getText());
		
		if (excecao.getErros().size() > 0) { // Teste na cole��o de erros, se h� algum erro.
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
			throw new IllegalStateException("Vendedor nulo!");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
	}
	
	private void setMensagemDeErros(Map<String, String> erros) { //M�todo para pegar os erros da exce��o e anexar na tela
		Set<String> fields = erros.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(erros.get("nome"));
		}
	}
}
