package warningGUIsManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class DeleteWarningController {
	@FXML
	private Button yesButton = new Button();
	private Button noButton = new Button();
	public static boolean yesDel=false;
	public void yesPressed(ActionEvent event){
		yesDel=true;	
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	public void noPressed(ActionEvent event){
		yesDel=false;
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
