package viewGUIsManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import mainGUIManager.MainGUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewController implements Initializable{
	@FXML
	private Button applyButton = new Button();
	@FXML
	private TextArea viewTextArea = new TextArea();
	@FXML
	private Button applyMultipleButton = new Button();
	public static String view;
	private String currentDb;
	private guiRequestManager.UpdateManager updater;
	
	public void setMainGUIController(String currentDb){
		this.currentDb=currentDb;
		viewTextArea.setWrapText(true);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	public void applyView(ActionEvent event){
		String update="use "+currentDb;
    	updater = new guiRequestManager.UpdateManager(update);
		update=viewTextArea.getText();
		updater = new guiRequestManager.UpdateManager(update);
		((Node) event.getSource()).getScene().getWindow().hide();
	}	
	public void chooseProjects(ActionEvent event){
		view=viewTextArea.getText();
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/ChooseProjects.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Choose Projects");
	        ChooseProjectsController cPc = loader.getController();
	        cPc.setMainGUIController(currentDb);
	        dialogStage.showAndWait();	   
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
