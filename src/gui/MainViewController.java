package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemAjuda;
	
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/listaDepartamento.fxml");
	}
	
	@FXML
	public void onMenuItemAjudaAction() {
		loadView("/gui/Ajuda.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	private synchronized void loadView(String nomeAbsoluto) { // Fun��o para abrir uma outra tela. synchronized = Garante que o processamento n�o ser� interrompido pelo Thread
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
		}
		catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro carregando a p�gina.", e.getMessage(), AlertType.ERROR);
		}
	}

}
