package dataGUIManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import warningGUIsManager.WarningsController;
import chartGUIsManager.BarChartMaker;
import chartGUIsManager.LineChartMaker;
import chartGUIsManager.ScatterChartMaker;
import mainGUIManager.MainGUIController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableController implements Initializable{
	@FXML
	private TableView dataTable;
	@FXML
	private Button makeScatterButton = new Button();
	@FXML
	private CheckBox xAxisCheckBox = new CheckBox();
	@FXML
	private CheckBox yAxisCheckBox = new CheckBox();
	@FXML
	private CheckBox seriesCheckBox = new CheckBox();
	@FXML
	private Label chartLabel = new Label();
	@FXML
	private Button isolateButton = new Button();
	@FXML
	private Button makeBarChartButton = new Button();
	@FXML
	private Button createLineChartButton = new Button();
	@FXML 
	private TextField chartTitleTextField = new TextField();
	private List<String> columns = new ArrayList<String>();
	private ArrayList<String> currentColumns;
	private TableViewSelectionModel tableModel;
	private ArrayList<String> series;
	private ArrayList<String> xValues;
	private ArrayList<String> yValues;
	private int currentCol;
	private int xAxCur=0;
	private int yAxCur=0;
	private int serCur=0;
	private String xAx= new String();
	private String yAx = new String();
	private String ser = new String();
	private String curTab = new String();
	private String curDb = new String();
	private String chartTitle = new String();
	private ArrayList<Integer> Columns = new ArrayList<Integer>();
	private ArrayList<Integer> Rows = new ArrayList<Integer>();
	private ArrayList<ObservableList<String>> rows;
	private guiRequestManager.DataProvider provider;
	
	public void setParent(String currentDb, String currentTable){
		curDb=currentDb;
		curTab=currentTable;
		initialize();
	}
	public void initialize(){
		dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		dataTable.getSelectionModel().setCellSelectionEnabled(true);
		tableModel=dataTable.getSelectionModel();
		final ObservableList<TablePosition> selectedCells = dataTable.getSelectionModel().getSelectedCells();
		selectedCells.addListener(new ListChangeListener<TablePosition>() {
		    @Override
		    public void onChanged(Change change) {
		    	Columns = new ArrayList<Integer>();
		    	Rows = new ArrayList<Integer>();
		        for (TablePosition pos : selectedCells) {
		        	Columns.add(pos.getColumn());
		        	Rows.add(pos.getRow());
		        	currentCol=pos.getColumn();
		        }
		    }
		});
		xAxisCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue==true){
			        yAxisCheckBox.setSelected(!newValue);
			        seriesCheckBox.setSelected(!newValue);
			        xAx=columns.get(currentCol);
			        xAxCur=currentCol;
			        chartLabel.setText("You chose xAxis: "+xAx+",  yAxis: "+yAx+",  series: "+ser);
		    	}
		    }
		});
		yAxisCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue==true){
			        xAxisCheckBox.setSelected(!newValue);
			        seriesCheckBox.setSelected(!newValue);
			        yAx=columns.get(currentCol);
			        yAxCur=currentCol;
			        chartLabel.setText("You chose xAxis: "+xAx+",  yAxis: "+yAx+",  series: "+ser);
		    	}
		    }
		});
		seriesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue==true){
			        yAxisCheckBox.setSelected(!newValue);
			        xAxisCheckBox.setSelected(!newValue);
			        ser=columns.get(currentCol);
			        serCur=currentCol;
			        chartLabel.setText("You chose xAxis: "+xAx+",  yAxis: "+yAx+",  series: "+ser);
		    	}
		    }
		});
		columns = new ArrayList<String>();
		currentColumns=new ArrayList<String>();
		String query1="show columns from "+curTab;
		provider= new guiRequestManager.DataProvider(query1);
		currentColumns=provider.executeQuery();
		String orderBy=null;
		rows = new ArrayList<ObservableList<String>>();
		ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
		provider = new guiRequestManager.DataProvider(curTab, currentColumns.size(),currentColumns.get(0));
		allResults=provider.selectAll();
		for(int i=0;i<allResults.size();i++){
			columns.add(currentColumns.get(i));
			TableColumn [] tableColumns = new TableColumn[columns.size()];     
	        int columnIndex = 0;
            final int m = i;
            TableColumn col = new TableColumn(columns.get(i));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(m).toString());                       
                }                   
            });
            col.setSortable(false);
            dataTable.getColumns().addAll(col);
	        if(i==0){ 
				for(int j=0;j<allResults.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(allResults.get(i).get(j));
					rows.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<allResults.get(i).size();j++){
					rows.get(j).addAll(allResults.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rows.size();l++){
			dataTable.getItems().add(rows.get(l));
		}
	}
	public void showGenWarning(String warning){
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
	public void makeScatter(ActionEvent event){
		if(xAx.equals("") || yAx.equals("")){
			showGenWarning("You have to set both xAxis and yAxis!");
		}
		else{
			chartTitle=chartTitleTextField.getText();
			series= new ArrayList<String>();
			xValues= new ArrayList<String>();
			yValues=new ArrayList<String>();
			if(!ser.equals("")){
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					series.add(tempArray[serCur].trim());
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			else{
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("/application/ScatterPlot.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("ScatterChart");
		        ScatterChartMaker sc = loader.getController();
				sc.setDataWin(xAx, yAx, ser, curTab, curDb,series,xValues, yValues, chartTitle);
		        dialogStage.show();
		        if(sc.valid==false){
		        	dialogStage.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			chartLabel.setText("");
			ser="";
			xAx="";
			yAx="";
			xAxisCheckBox.setSelected(false);
			yAxisCheckBox.setSelected(false);
			seriesCheckBox.setSelected(false);
			chartTitleTextField.setText("");
		}
	}
	public void isolateData(ActionEvent event){
		int prevSize = dataTable.getItems().size();
		ObservableList<TableColumn> cols=dataTable.getColumns();
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		for(int i=0;i<Columns.size();i++){
			ArrayList<String> temp = new ArrayList<String>();
			for(int j=0;j<dataTable.getItems().size();j++){
				temp.add(cols.get(Columns.get(i)).getCellData(j).toString());
			}
			values.add(temp);
		}
		columns = new ArrayList<String>();
		rows = new ArrayList<ObservableList<String>>();
		for(int i=0;i<Columns.size();i++){
			columns.add("-"+cols.get(Columns.get(i)).getText()+"-");
			TableColumn [] tableColumns = new TableColumn[columns.size()];     
	        int columnIndex = 0;
            final int m = i;
            TableColumn col = new TableColumn(columns.get(i));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                   
               public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                             
                    return new SimpleStringProperty(param.getValue().get(m).toString());                       
                }                   
            });
            col.setSortable(false);
            col.setEditable(true);
            dataTable.getColumns().addAll(col);
           
	        if(i==0){ 
				for(int j=0;j<values.get(i).size();j++){
					ObservableList<String> row = FXCollections.observableArrayList();
					row.addAll(values.get(i).get(j));
					rows.add(row);
				}
	        }
	        else{
	        	for(int j=0;j<values.get(i).size();j++){
					rows.get(j).addAll(values.get(i).get(j));
				}
	        }
		}
		for(int l=0;l<rows.size();l++){
			dataTable.getItems().add(rows.get(l));
		}
		int tem=dataTable.getColumns().size()-Columns.size();		
		for(int i=0;i<tem;i++){
			dataTable.getColumns().remove(0);
		}
		for(int i=0;i<prevSize;i++){
			dataTable.getItems().remove(0);
		}
	}
	public void makeBarChart(ActionEvent event){
		if(xAx.equals("") || yAx.equals("")){
			showGenWarning("You have to set both xAxis and yAxis!");
		}
		else{
			chartTitle=chartTitleTextField.getText();
			series= new ArrayList<String>();
			xValues= new ArrayList<String>();
			yValues=new ArrayList<String>();
			if(!ser.equals("")){
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					series.add(tempArray[serCur].trim());
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			else{
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("/application/BarChart.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("BarChart");
		        BarChartMaker bc = loader.getController();
				bc.setDataWin(xAx, yAx, ser, curTab, curDb,series,xValues,yValues, chartTitle);
		        dialogStage.show();
		       if(bc.valid==false){
		        	dialogStage.close();
		        }
		    }catch (IOException e) {
		        e.printStackTrace();
		    }
			chartLabel.setText("");
			ser="";
			xAx="";
			yAx="";
			xAxisCheckBox.setSelected(false);
			yAxisCheckBox.setSelected(false);
			seriesCheckBox.setSelected(false);
			chartTitleTextField.setText("");
		}
	}
	public void makeLineChart(ActionEvent event){
		if(xAx.equals("") || yAx.equals("")){
			showGenWarning("You have to set both xAxis and yAxis!");
		}
		else{
			chartTitle=chartTitleTextField.getText();
			series= new ArrayList<String>();
			xValues= new ArrayList<String>();
			yValues=new ArrayList<String>();
			if(!ser.equals("")){
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					series.add(tempArray[serCur].trim());
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			else{
				for(int i=0;i<dataTable.getItems().size();i++){
					String[] tempArray=dataTable.getItems().get(i).toString().replace("[", "").replace("]", "").split(",");
					xValues.add(tempArray[xAxCur].trim());
					yValues.add(tempArray[yAxCur].trim());
				}
			}
			try{
				FXMLLoader loader = new FXMLLoader();
		        loader.setLocation((getClass().getResource("/application/LineChart.fxml")));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);
		        dialogStage.setTitle("LineChart");
		        LineChartMaker lc = loader.getController();
		        lc.setDataWin(xAx, yAx, ser, curTab, curDb,series, xValues, yValues,chartTitle);
		        dialogStage.show();
		       if(lc.valid==false){
		        	dialogStage.close();
		        }
		    }catch (IOException e) {
		        e.printStackTrace();
		    }
			chartLabel.setText("");
			ser="";
			xAx="";
			yAx="";
			xAxisCheckBox.setSelected(false);
			yAxisCheckBox.setSelected(false);
			seriesCheckBox.setSelected(false);
			chartTitleTextField.setText("");
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
}
