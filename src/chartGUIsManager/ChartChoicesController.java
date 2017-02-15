package chartGUIsManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import warningGUIsManager.WarningsController;
import mainGUIManager.MainGUIController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChartChoicesController implements Initializable{
	@FXML 
	private TextField seriesNameTextField;
	@FXML
	private ChoiceBox seriesColumnChoiceBox = new ChoiceBox();
	@FXML
	private ChoiceBox xColumnsChoiceBox = new ChoiceBox();
	@FXML
	private ChoiceBox yColumnsChoiceBox = new ChoiceBox();
	@FXML
	private Button addSeriesButton;
	@FXML
	private Button deleteSeriesButton;
	@FXML
	private ListView selectedSeriesListView = new ListView<String>();
	@FXML 
	private Button createScatterPlotButton;
	@FXML
	private ChoiceBox projectChoiceBox = new ChoiceBox();
	@FXML
	private ChoiceBox tableChoiceBox = new ChoiceBox();
	@FXML
	private TextField nameTextField = new TextField();
	@FXML
	private Button createBarChartButton = new Button();
	@FXML
	private Button createLineChartButton = new Button();
	private ObservableList<String> axisItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> seriesModel;
	private SingleSelectionModel<String> xModel;
	private SingleSelectionModel<String> yModel;
	private SingleSelectionModel<String> projectModel;
	private SingleSelectionModel<String> tableModel;
	private ObservableList<String> projectItems = FXCollections.observableArrayList();
	private ObservableList<String> tableItems = FXCollections.observableArrayList();
	private MultipleSelectionModel<String> listViewModel;
	private ArrayList<String> projects = new ArrayList<String>();
	private ArrayList<String> xTables = new ArrayList<String>();
	private ArrayList<String> yTables = new ArrayList<String>();
	private ArrayList<String> series = new ArrayList<String>();
	private ArrayList<String> xAxis = new ArrayList<String>();
	private ArrayList<String> yAxis = new ArrayList<String>();
	private String chartName = new String();
	private boolean isSimple=true;
	private String seriesColumn= new String();
	private ArrayList<String> currentColumns = new ArrayList<String>();
	private ArrayList<String> currentProjects = new ArrayList<String>();
	private ArrayList<String> currentTables = new ArrayList<String>();
	private ObservableList<String> selectedListItems = FXCollections.observableArrayList();
	private String currentXTable = new String();
	private String currentYTable = new String();
	private String currentSTable = new String();
	private int xAx=-1;
	private int yAx=-1;
	private boolean clearSFlag=false;
	private boolean clearXFlag=false;
	private boolean clearYFlag=false;
	private guiRequestManager.UpdateManager updater;
	private guiRequestManager.DataProvider provider;
	
	public void getProjects(){
		Scanner inputReader;
		String path="existingProjects/Projects.txt";
		File file = new File(path);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
			 while(inputReader.hasNextLine()){
					String line=inputReader.nextLine();
					currentProjects.add(line);
			 }
			 inputReader.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",path); 
		 }
	}
	public void initialize() {
		seriesModel=seriesColumnChoiceBox.getSelectionModel();
		projectModel=projectChoiceBox.getSelectionModel();
		tableModel=tableChoiceBox.getSelectionModel();
		xModel=xColumnsChoiceBox.getSelectionModel();
		yModel=yColumnsChoiceBox.getSelectionModel();
		getProjects();
		for(int i=0;i<currentProjects.size();i++){
			projectItems.add(currentProjects.get(i));
		}
		projectChoiceBox.setItems(projectItems);
		projectChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
	            	tableModel.clearSelection();
	            	xModel.clearSelection();
	            	yModel.clearSelection();
	            	seriesModel.clearSelection();
	            	tableItems = FXCollections.observableArrayList();
	            	axisItems = FXCollections.observableArrayList();
	            	String query1="show tables from "+projectItems.get(new_value.intValue());
	            	provider= new guiRequestManager.DataProvider(query1);
	        		currentTables=provider.executeQuery();
	            	for(int i=0;i<currentTables.size();i++){
	        			tableItems.add(currentTables.get(i));
	        		}
	        		tableChoiceBox.setItems(tableItems);
            	}catch (Exception e) {}
            }
          });
		tableChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try {
            		String update="use "+projectModel.getSelectedItem().toString();
                	updater = new guiRequestManager.UpdateManager(update);
                	String query1="show columns from "+tableItems.get(new_value.intValue());
            		provider= new guiRequestManager.DataProvider(query1);
            		currentColumns=provider.executeQuery();
					axisItems = FXCollections.observableArrayList();
					for(int i=0;i<currentColumns.size();i++){
						axisItems.add(currentColumns.get(i));
					}
					if(seriesModel.getSelectedIndex()>-1 && xModel.getSelectedIndex()<0 && yModel.getSelectedIndex()<0){
						xColumnsChoiceBox.setItems(axisItems);
						yColumnsChoiceBox.setItems(axisItems);
					}
					else if(seriesModel.getSelectedIndex()>-1 && xModel.getSelectedIndex()>-1 && yModel.getSelectedIndex()<0){
						yColumnsChoiceBox.setItems(axisItems);
					}
					else if(seriesModel.getSelectedIndex()>-1 && xModel.getSelectedIndex()<0 && yModel.getSelectedIndex()>-1){
						xColumnsChoiceBox.setItems(axisItems);
					}
					else if(seriesModel.getSelectedIndex()<0 && xModel.getSelectedIndex()>-1 && yModel.getSelectedIndex()<0){
						seriesColumnChoiceBox.setItems(axisItems);
						yColumnsChoiceBox.setItems(axisItems);
					}
					else if(seriesModel.getSelectedIndex()<0 && xModel.getSelectedIndex()>-1 && yModel.getSelectedIndex()>-1){
						seriesColumnChoiceBox.setItems(axisItems);
					}
					else if(seriesModel.getSelectedIndex()<0 && xModel.getSelectedIndex()<0 && yModel.getSelectedIndex()>-1){
						seriesColumnChoiceBox.setItems(axisItems);
						xColumnsChoiceBox.setItems(axisItems);
					}
					else{
						seriesColumnChoiceBox.setItems(axisItems);
						xColumnsChoiceBox.setItems(axisItems);
						yColumnsChoiceBox.setItems(axisItems);
					}	
            	} catch (Exception e) {}
            }
          });
		xColumnsChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		if(clearXFlag==false){
	            		currentXTable=tableModel.getSelectedItem().toString();
	            		xAx=xColumnsChoiceBox.getSelectionModel().getSelectedIndex();
            		}
            	}catch (Exception e) {}
            }
          });
		yColumnsChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		if(clearYFlag==false){
	            		currentYTable=tableModel.getSelectedItem().toString();
	            		yAx=yColumnsChoiceBox.getSelectionModel().getSelectedIndex();
            		}
            	}catch (Exception e) {}
            }
          });
		seriesColumnChoiceBox.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
            	try{
            		if(clearSFlag==false){
            			currentSTable=tableModel.getSelectedItem().toString();
            		}
            	}catch (Exception e) {}
            }
          });
		selectedSeriesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	public void showGenWarning(){
		String warning ="You have to set both xAxis and yAxis!";
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
	public void addSeries(ActionEvent event){
		projects.add(projectModel.getSelectedItem().toString());
		if(xAx<0 || yAx<0){
			showGenWarning();
		}
		else{
			xAxis.add(xModel.getSelectedItem().toString());
			yAxis.add(yModel.getSelectedItem().toString());
			selectedListItems = FXCollections.observableArrayList();
			if(seriesModel.getSelectedIndex()<0){
				xTables.add(currentXTable);
				yTables.add(currentYTable);
				isSimple=true;
				series.add(seriesNameTextField.getText());
				for(int i=0;i<series.size();i++){
					String item="*"+series.get(i)+"*   xAxis: "+xAxis.get(i)+" from "+xTables.get(i)+"   ,yAxis: "+yAxis.get(i)+" from "+yTables.get(i);
					selectedListItems.add(item);
				}
				seriesNameTextField.setText(" ");
			}
			else{
				isSimple=false;
				seriesColumn=seriesModel.getSelectedItem().toString();
				String item="*"+seriesColumn+" from "+currentSTable+"*   xAxis: "+xAxis.get(0)+" from "+currentXTable+"   ,yAxis: "+yAxis.get(0)+" from "+currentYTable;
				selectedListItems.add(item);
			}
			selectedSeriesListView.setItems(selectedListItems);
			clearSFlag=true;
			seriesModel.clearSelection();
			clearSFlag=false;
			clearXFlag=true;
			xModel.clearSelection();
			clearXFlag=false;
			clearYFlag=true;
			yModel.clearSelection();
			clearYFlag=false;
			projectModel.clearSelection();
			tableModel.clearSelection();
			tableItems = FXCollections.observableArrayList();
	    	axisItems = FXCollections.observableArrayList();
	    	tableChoiceBox.setItems(tableItems);
	    	xColumnsChoiceBox.setItems(axisItems);
	    	yColumnsChoiceBox.setItems(axisItems);
	    	xAx=-1;
			yAx=-1;
		}
	}
	public void deleteSeries(ActionEvent event){
		listViewModel=selectedSeriesListView.getSelectionModel();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for(int i=0;i<listViewModel.getSelectedIndices().size();i++){
			selected.add(listViewModel.getSelectedIndices().get(i));
			projects.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			if(isSimple==true){
				xTables.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
				yTables.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
				series.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			}
			else{
				xAxis.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
				yAxis.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			}
		}
		for(int i=0;i<selected.size();i++){
			selectedListItems.remove((int)(selected.get(i))-i);
		}
		selectedSeriesListView.setItems(selectedListItems);
		listViewModel.clearSelection();
	}
	public void createScatter(ActionEvent event){
		chartName=nameTextField.getText();
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/ScatterPlot.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("ScatterChart");
	        ScatterChartMaker sc = loader.getController();
			sc.setDataOpt(isSimple,chartName,projects,xTables,yTables,series,xAxis,yAxis,seriesColumn,
						  currentXTable,currentYTable,currentSTable);
	        dialogStage.show();	   
	        if(ScatterChartMaker.valid==false){
	        	dialogStage.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		reset();
	}
	public void createBarChart(ActionEvent event){selectedListItems.clear();
		chartName=nameTextField.getText();
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/BarChart.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("BarChart");
	        BarChartMaker bc = loader.getController();
			bc.setDataOpt(isSimple,chartName,projects,xTables,yTables,series,xAxis,yAxis,seriesColumn,
						  currentXTable,currentYTable,currentSTable); 
	        dialogStage.show();	   
	        if(BarChartMaker.valid==false){
	        	dialogStage.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		reset();
	}
	public void createLineChart(ActionEvent event){
		chartName=nameTextField.getText();
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/LineChart.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setTitle("LineChart");
	        LineChartMaker lc = loader.getController();
			lc.setDataOpt(isSimple,chartName,projects,xTables,yTables,series,xAxis,yAxis,seriesColumn,
						  currentXTable,currentYTable,currentSTable);
	        dialogStage.show();	   
	        if(LineChartMaker.valid==false){
	        	dialogStage.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		reset();
	}
	public void reset(){
		for(int i=0;i<=selectedListItems.size();i++){
			try{
				selectedListItems.remove(0);
			}catch(Exception e){}
		}
		isSimple=true;
		chartName="";
		xAxis= new ArrayList<String>();
		yAxis= new ArrayList<String>();
		seriesColumn="";
		currentXTable="";
		currentYTable="";
		projects=new ArrayList<String>();
		series=new ArrayList<String>();
		xTables=new ArrayList<String>();
		yTables=new ArrayList<String>();
		selectedSeriesListView.setItems(selectedListItems);
		nameTextField.setText("");
		xAx=-1;
		yAx=-1;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources){}
}
