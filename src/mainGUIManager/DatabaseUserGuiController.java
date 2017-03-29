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
import javafx.scene.control.PasswordField;
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
	private PasswordField passwordTextfield;
	@FXML
	private CheckBox saveOptionsCheckbox;
	private String username=new String();
	private String password=new String();
	private boolean saved=false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	public void submitPressed(ActionEvent event) throws IOException{
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
			if(username.equals("")){
				username=" ";
			}
			if(password.equals("")){
				password=" ";
			}
			outputWriter.append(username+";"+password+"\n");
			outputWriter.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
	}
}
