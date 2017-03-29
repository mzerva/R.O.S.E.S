package mainGUIManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import warningGUIsManager.WarningsController;

public class DatabaseUserGuiController implements Initializable {
	@FXML
	private Button submitButton;
	@FXML
	private TextField usernameTextfield;
	@FXML
	private TextField passwordTextfield;
	@FXML
	private CheckBox saveOptionsCheckbox;
	private String username=new String();
	private String password=new String();
	private boolean saved=false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	public void submitPressed(ActionEvent event) throws IOException{
		/*if(usernameTextfield.getText().equals("") || passwordTextfield.getText().equals("")){
			String warning="No name for project!";
			showWarning(warning);
		}*/
		//System.out.println("Pressed");
		username=usernameTextfield.getText();
		password=passwordTextfield.getText();
		if(saveOptionsCheckbox.isSelected()){
			saved=true;
			savePassword();
		}
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public void savePassword() throws IOException{
		PrintWriter outputWriter;
		String output="existingProjects/DBPassword.txt";
		File file = new File(output);
		try 
		 { 
			outputWriter = new PrintWriter(new FileWriter(file, true));
			outputWriter.append(username+";"+password+"\n");
			outputWriter.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
	}
	/*public void showWarning(String warning){
    	try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/Warnings.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Warning");
	        WarningsController wC = loader.getController();
	        wC.setWarning(warning);
	        dialogStage.showAndWait();	   
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }*/
}
