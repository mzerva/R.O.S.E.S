package mainGUIManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import chartGUIsManager.ChartChoicesController;
import dataGUIManager.TableController;
import viewGUIsManager.ViewController;
import warningGUIsManager.DeleteWarningController;
import warningGUIsManager.WarningsController;
import mainInfoManager.MainInfo;
//import databaseManager.CentralSQLEngine;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class MainGUIController implements Initializable {
	@FXML
	private Button loadFileButton;
	@FXML
	private Button createProjectButton;
	@FXML
	private TextField projectNameTextField;
	private File file;
	@FXML
	private ChoiceBox projectChoiceBox= new ChoiceBox();
	@FXML
	private ChoiceBox dataChoiceBox= new ChoiceBox();
	@FXML
	private Button viewDataButton = new Button();
	@FXML 
	private Button chooseProjectButton;
	@FXML
	private Button createViewButton;
	@FXML
	private TextField createViewTextField;
	@FXML
	private Button deleteProjectButton = new Button();
	@FXML
	private Button deleteDataButton = new Button();
	@FXML
	private Button deleteViewButton = new Button();
	@FXML
	private Button viewDataWindowButton = new Button();
	@FXML
	private Button chartOptionsButton = new Button();
	@FXML
	private Label currentTableLabel = new Label();
	@FXML
	private Label currentLabel = new Label();
	private ObservableList<String> projectItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> model;
	private ObservableList<String> dataItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> modelData;
	private ArrayList<File> tempFiles;
	private List<File> mulFiles;
	private int stat=0;
	private int metric=0;
	private ArrayList<String> inputFiles;
	private String currentDb= new String();
	private String currentTable = new String();
	private ArrayList<String> existingProjects = new ArrayList<String>();
	private guiRequestManager.UpdateManager updater;
	private guiRequestManager.DataProvider provider;
	private String user=new String();
	private String password=new String();
		
	@Override
	public void initialize(URL location, ResourceBundle resources){
		if(!checkForDBPasswords()){
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("/application/DatabaseUser.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("Database Passwords");
		        dialogStage.showAndWait();	 
		        if(checkForDBPasswords()){
		        	ini();
		        }
		        else{
		        	DatabaseUserGuiController dbC = loader.getController();
			        user=dbC.getUsername();
			        password=dbC.getPassword();
			        ini();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		else{
			ini();
		}
		
	}
	public void ini(){
		MainInfo.init(user,password);
		model=projectChoiceBox.getSelectionModel();	
		modelData=dataChoiceBox.getSelectionModel();
		refresh();
	}
	public boolean checkForDBPasswords(){
		Scanner inputReader;
		String path="existingProjects/DBPassword.txt";
		File file = new File(path);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
			 boolean flag=false;
			 while(inputReader.hasNextLine()){
				    String line=inputReader.nextLine();
				    if(!line.equals("")){
				    	flag=true;
					    String[] dbCodes=line.split(";");
					    if(dbCodes[0].equals(" ")){
					    	user="";
					    }
					    else{
					    	user=dbCodes[0];
					    }
					    if(dbCodes[1].equals(" ")){
					    	password="";
					    }
					    else{
						    password=dbCodes[1];    
					    }
				    }
			 }
			 inputReader.close();
			 if(flag==false){
				 return false;
			 }
			 else{
				 return true;
			 }
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",path); 
			 return false;
		 }
	}
	public void dataViewer(){
		dataItems = FXCollections.observableArrayList();
		ArrayList<String> result1 = new ArrayList<String>();
		String query1="show tables";
		provider= new guiRequestManager.DataProvider(query1);
		result1=provider.executeQuery();
		for(int i=0;i<result1.size();i++){
			dataItems.add(result1.get(i));
		}
		dataChoiceBox.setItems(dataItems);
	}
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
	public void refresh(){
		projectItems = FXCollections.observableArrayList();
		existingProjects=new ArrayList<String>();
		showProjects();
		for(int i=0;i<existingProjects.size();i++){
			projectItems.add(existingProjects.get(i));
		}
		projectChoiceBox.setItems(projectItems);
		projectChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		chooseProject(projectItems.get(new_value.intValue()));
            	}catch (Exception e) {}
            }
          });
		dataChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		currentTable=dataItems.get((int) new_value);
            		currentTableLabel.setText("and table: "+currentTable);
            	}catch (Exception e) {}
            }
          });
	}
	public void loadFile(ActionEvent event) throws IOException, SQLException{
		try{
			tempFiles= new ArrayList<File>();
			inputFiles= new ArrayList<String>();
			FileChooser fileChooser = new FileChooser();
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
	        fileChooser.getExtensionFilters().add(extFilter);
	        mulFiles = fileChooser.showOpenMultipleDialog(null);
	        for(int i=0;i<mulFiles.size();i++){
	        	String update="use "+currentDb;
	        	updater = new guiRequestManager.UpdateManager(update);
			    if(mulFiles.get(i).getName().equals("table_stats.csv")){
	        		stat=i;
	        		inputFiles.add(mulFiles.get(i).getPath());
	        	}
			    else if(mulFiles.get(i).getName().equals("metrics.csv")){
	        		metric=i;
	        		inputFiles.add(mulFiles.get(i).getPath());
	        	}
			    else if(mulFiles.get(i).getName().equals("all.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 5);
					inputFiles.add(mulFiles.get(i).getPath()+".txt&"+mulFiles.get(i).getPath()+"1.txt");
					fc.processAll();
					tempFiles.add(fc.getFile1());
					tempFiles.add(fc.getFile2());
				}
				else if(mulFiles.get(i).getName().equals("table_del.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 1);
					inputFiles.add(mulFiles.get(i).getPath()+".txt");
					fc.process();
					tempFiles.add(fc.getFile1());
				}
				else if(mulFiles.get(i).getName().equals("table_ins.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 2);
					inputFiles.add(mulFiles.get(i).getPath()+".txt");
					fc.process();
					tempFiles.add(fc.getFile1());
				}
				else if(mulFiles.get(i).getName().equals("table_key.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 3);
					inputFiles.add(mulFiles.get(i).getPath()+".txt");
					fc.process();
					tempFiles.add(fc.getFile1());
				}
				else if(mulFiles.get(i).getName().equals("table_type.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 4);
					inputFiles.add(mulFiles.get(i).getPath()+".txt");
					fc.process();	
					tempFiles.add(fc.getFile1());
				}
				else if(mulFiles.get(i).getName().equals("tables.csv")){
					FileConverter fc = new FileConverter(mulFiles.get(i).getPath(), 0);
					inputFiles.add(mulFiles.get(i).getPath()+".txt");
					fc.process();
					tempFiles.add(fc.getFile1());
				}
				else{
					inputFiles.add(mulFiles.get(i).getPath());
				}
	        }
	        if(mulFiles.size()>1){
	        	loadMoreFiles();
	        }
	        else{
	        	loadOneFile();
	        }
	        for(int i=0;i<tempFiles.size();i++){
	        	tempFiles.get(i).delete();
	        }
		}
		catch(Exception e){
			String warning="Loading files not completed!";
			showWarning(warning);
		}
	}
	public void addProject(String projectName) throws IOException{
		PrintWriter outputWriter;
		String output="existingProjects/Projects.txt";
		File file = new File(output);
		try 
		 { 
			outputWriter = new PrintWriter(new FileWriter(file, true));
			outputWriter.append(projectName+"\n");
			outputWriter.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
	}
	public void createProject(ActionEvent event) throws IOException{
		if(projectNameTextField.getText().equals("")){
			String warning="No name for project!";
			showWarning(warning);
		}
		else{
			String projectName=new String();
			projectName=projectNameTextField.getText();	
			currentDb=projectName;
			String update="create database "+projectName;
			try {
	        	updater = new guiRequestManager.UpdateManager(update);
	        	updater = new guiRequestManager.UpdateManager(currentDb, MainInfo.filePath);
				addProject(projectName);
			} catch (SQLException e){}
			refresh();
			update="use "+currentDb;
	    	updater = new guiRequestManager.UpdateManager(update);
			dataViewer();
			projectNameTextField.setText("");
			currentLabel.setText("You are currently working with project: "+currentDb);
			currentTableLabel.setText("");
		}
	}
	public void loadOneFile() throws IOException, SQLException{
		for(int i=0;i<mulFiles.size();i++){
        	updater = new guiRequestManager.UpdateManager(currentDb, MainInfo.files.get(mulFiles.get(i).getName()), inputFiles.get(i));
    	}
	}
	public void loadMoreFiles() throws IOException, SQLException{
		updater = new guiRequestManager.UpdateManager(currentDb, MainInfo.files.get(mulFiles.get(stat).getName()), inputFiles.get(stat));
    	updater = new guiRequestManager.UpdateManager(currentDb, MainInfo.files.get(mulFiles.get(metric).getName()), inputFiles.get(metric));
    	for(int i=0;i<mulFiles.size();i++){
    		if(i!=stat && i!=metric){
            	updater = new guiRequestManager.UpdateManager(currentDb, MainInfo.files.get(mulFiles.get(i).getName()), inputFiles.get(i));
    		}
    	}
	}
	public void viewDataInWindow(ActionEvent event){
		if(modelData.getSelectedIndex()==-1){
			String warning="No data selected!";
			showWarning(warning);
		}
		else{
			currentTable=modelData.getSelectedItem().toString();
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("/application/Table.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);
		        dialogStage.setTitle(currentTable+" from "+currentDb);
		        TableController tc = loader.getController();
		        tc.setParent(currentDb, currentTable);
		        dialogStage.show();	   
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	public void chooseProject(String db){
		currentDb=db;
		String update="use "+currentDb;
    	updater = new guiRequestManager.UpdateManager(update);
		dataViewer();
		currentLabel.setText("You are currently working with project: "+currentDb);
		currentTableLabel.setText("");
	}
	public void createView(ActionEvent event){
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/Views.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Create a View");
	        ViewController vC = loader.getController();
	        vC.setMainGUIController(/*this,*/currentDb);
	        dialogStage.showAndWait();	   
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		dataViewer();
	}
	public void eraseProject(String proj){
		if(existingProjects.contains(proj)){
			existingProjects.remove(proj);
		}
		PrintWriter outputWriter;
		String output="existingProjects/Projects.txt";
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(output));
			for(int i=0;i<existingProjects.size();i++){
				outputWriter.println(existingProjects.get(i));
			}
			outputWriter.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
	}
	public void deleteProject(ActionEvent event){
		if(model.getSelectedIndex()==-1){
			String warning="No Project Selected!";
			showWarning(warning);
		}
		else{
			showDelWarning();
			if(DeleteWarningController.yesDel==true){
				String update="drop database "+model.getSelectedItem().toString();
		    	updater = new guiRequestManager.UpdateManager(update);
				eraseProject(model.getSelectedItem().toString());
				refresh();
			}
			currentLabel.setText("");
			currentTableLabel.setText("");
			dataItems = FXCollections.observableArrayList();
			dataChoiceBox.setItems(dataItems);
		}
	}
	public void showDelWarning(){
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/DeleteWarning.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Warning");
	        dialogStage.showAndWait();	   
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void showWarning(String warning){
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
    }
	public void deleteData(ActionEvent event){
		if(modelData.getSelectedIndex()==-1){
			String warning="No Table Selected!";
			showWarning(warning);
		}
		else{
			showDelWarning();
			if(DeleteWarningController.yesDel==true){
				if(DeleteWarningController.yesDel==true){
					String update="delete from "+modelData.getSelectedItem().toString();
			    	updater = new guiRequestManager.UpdateManager(update);
				}
			}
		}
	}
	public void deleteView(ActionEvent event){
		if(modelData.getSelectedIndex()==-1){
			String warning="No View Selected!";
			showWarning(warning);
		}
		else{
			showDelWarning();
			if(DeleteWarningController.yesDel==true){
				String update="drop view "+modelData.getSelectedItem().toString();
		    	updater = new guiRequestManager.UpdateManager(update);
				dataViewer();
			}
			currentTableLabel.setText("");
		}
	}
	public void showOptions(ActionEvent event){
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/ChartChoices.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("Chart Options");
	        ChartChoicesController cCC = loader.getController();
	        cCC.initialize();
	        dialogStage.show();	   
	    }
		catch (IOException e){
	        e.printStackTrace();
	    }
	}
}
