package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.ServicoDepartamento;
import model.service.ServicoVendedor;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemAjuda;
	
	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/listaVendedor.fxml", (ControllerListaVendedor controller) -> {
			controller.setServicoVendedor(new ServicoVendedor());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/listaDepartamento.fxml", (ControllerListaDepartamento controller) -> {
			controller.setServicoDepartamento(new ServicoDepartamento());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAjudaAction() {
		loadView("/gui/Ajuda.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	private synchronized <T> void loadView(String nomeAbsoluto, Consumer<T> initializingAction) { // Fun��o para abrir uma outra tela. synchronized = Garante que o processamento n�o ser� interrompido pelo Thread
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto)); // Padr�o do m�todo ' (getClass().getResource(nomeAbsoluto)) '
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox =(VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			/*
			 * Com isso � poss�vel manipular a cena principal
			 * e incluir o Main Menu e os filhos da janela que est� sendo aberta pelo m�todo.
			 */
			
			T controller = loader.getController(); // Ir�o executar a fun��o que est� sendo passada como argumento no m�todo 'onMenuItemDepartamentoAction' entre outros m�todos
			initializingAction.accept(controller); // Ir�o executar a fun��o que est� sendo passada como argumento no m�todo 'onMenuItemDepartamentoAction' entre outros m�todos
		}
		catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro carregando a p�gina.", e.getMessage(), AlertType.ERROR);
		}
	}
}
