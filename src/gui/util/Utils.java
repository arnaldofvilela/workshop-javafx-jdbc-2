package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	// palco atual = currentStage
	// ActionEvent = evento do botão
	// getSource é do tipo object -> muito genérico
	// faz-se o downcasting dele para o tipo Node
	// a partir desse node chama a função getScene
	// getWindow para pegar a janela
	// essa janela é uma super classe do stage
	// então faz-se o downcasting pra Stage
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
