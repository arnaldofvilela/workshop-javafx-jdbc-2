package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	// palco atual = currentStage
	// ActionEvent = evento do bot�o
	// getSource � do tipo object -> muito gen�rico
	// faz-se o downcasting dele para o tipo Node
	// a partir desse node chama a fun��o getScene
	// getWindow para pegar a janela
	// essa janela � uma super classe do stage
	// ent�o faz-se o downcasting pra Stage
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
