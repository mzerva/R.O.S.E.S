package viewGUIsManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import mainGUIManager.MainGUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;

public class ChooseProjectsController implements Initializable{
	@FXML
	private Button applyButton = new Button();
	@FXML
	private ListView<String> projectListView = new ListView<String>();
	private ObservableList<String> projectListItems = FXCollections.observableArrayList();
	private MultipleSelectionModel<String> mulProjects;
	private String currentDb;
	private ArrayList<String> existingProjects = new ArrayList<String>();
	private guiRequestManager.UpdateManager updater;

	public void setMainGUIController(String currentDb){
		this.currentDb=currentDb;
		initialize();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	public void showProjects(){
		Scanner inputReader;
		String path="existingProjects/Projects.txt";
		File file = new File(path);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
			 while(inputReader.hasNextLine()){
					String line=inputReader.nextLine();
					existingProjects.add(line);
			 }
			 inputReader.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",path); 
		 }
	}
	public void initialize(){
		projectListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		showProjects();
		for(int i=0;i<existingProjects.size();i++){
			projectListItems.add(existingProjects.get(i));
		}
			projectListView.setItems(projectListItems);
	}
	public void applyToProjects(ActionEvent event){
		mulProjects=projectListView.getSelectionModel();
		List<String> choices= (List<String>) mulProjects.getSelectedItems();
		for(int i=0;i<choices.size();i++){
			String update="use "+choices.get(i);
	    	updater = new guiRequestManager.UpdateManager(update);
			update=ViewController.view;
			updater = new guiRequestManager.UpdateManager(update);
		}
		String update1="use "+currentDb;
    	updater = new guiRequestManager.UpdateManager(update1);
		((Node) event.getSource()).getScene().getWindow().hide();
	}	
}
