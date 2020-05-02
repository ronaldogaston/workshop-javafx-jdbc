package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) { // Serve para deixar essa nova janela como principal, sem podendo editar as demais telas se essa não for encerrada
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}

}
