package chartGUIsManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import warningGUIsManager.WarningsController;
import mainGUIManager.MainGUIController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ScatterChartMaker implements Initializable{
	 @FXML
     private NumberAxis xAxis = new NumberAxis();
	 @FXML
	 private NumberAxis yAxis = new NumberAxis();
	 @FXML
     private ScatterChart scatterChart = new ScatterChart(xAxis,yAxis);
	 @FXML
	 private Button saveChartButton = new Button();
	 private XYChart.Series<Number, Number> series;
	 private ArrayList<String> distinctValues = new ArrayList<String>();
	 public static boolean valid=true;
	 private String xAx;
	 private String yAx;
	 private String ser;
	 private String curTab;
	 private String curDb;
	 private ArrayList<String> seriesColumn = new ArrayList<String>();
	 private ArrayList<String> xValues = new ArrayList<String>();
	 private ArrayList<String> yValues = new ArrayList<String>();
	 private String chartTitle;
	 private boolean isSimple;
	 private ArrayList<String> projects = new ArrayList<String>();
	 private ArrayList<String> xTables = new ArrayList<String>();
	 private ArrayList<String> yTables = new ArrayList<String>();
	 private ArrayList<String> seriesOpt = new ArrayList<String>();
	 private ArrayList<String> xAxisOpt = new ArrayList<String>();
	 private ArrayList<String> yAxisOpt = new ArrayList<String>();
	 private String seriesCol= new String();
	 private String currentXTable = new String();
	 private String currentYTable = new String();
	 private String currentSTable = new String();
	 private ArrayList<SeriesFormat> seriesFormats = new ArrayList<SeriesFormat>();
	 private guiRequestManager.UpdateManager updater;
	 private guiRequestManager.DataProvider provider;

	 @Override
	 public void initialize(URL location, ResourceBundle resources) {}
	 public void setDataWin(String xAx,String yAx,String ser, String curTab, String curDb, ArrayList<String> seriesColumn,ArrayList<String> xValues, ArrayList<String> yValues,String chartTitle){
		    xAxis.tickLabelFontProperty().set(Font.font(15));
		    yAxis.tickLabelFontProperty().set(Font.font(15));
		 	this.xAx=xAx;
			this.yAx=yAx;
			this.ser=ser;
			this.curTab=curTab;
			this.curDb=curDb;
			this.seriesColumn=seriesColumn;
			this.chartTitle=chartTitle;
			this.xValues=xValues;
			this.yValues=yValues;
			valid=true;
			scatterChart.setTitle(chartTitle);
			xAxis.setStyle(" -fx-font-size: 20pt ;");
			yAxis.setStyle(" -fx-font-size: 20pt ;");
			mainScatter();		
	}
	 public void setDataOpt(boolean isSimple, String chartName, ArrayList<String> projects, ArrayList<String> xTables,
			 ArrayList<String> yTables,  ArrayList<String> seriesOpt, ArrayList<String> xAxisOpt , ArrayList<String> yAxisOpt,
			 String seriesCol, String currentXTable, String currentYTable, String currentSTable){
		xAxis.tickLabelFontProperty().set(Font.font(15));
		yAxis.tickLabelFontProperty().set(Font.font(15));
	 	this.projects = projects;
		this.xTables = xTables;
		this.yTables = yTables;
		this.seriesOpt = seriesOpt;
		this.xAxisOpt = xAxisOpt;
		this.yAxisOpt = yAxisOpt;
		this.seriesCol= seriesCol;
		this.currentXTable = currentXTable;
		this.currentYTable = currentYTable;
		this.currentSTable = currentSTable;
		 this.isSimple=isSimple;
		 this.chartTitle=chartName;
		 xAxis.setStyle(" -fx-font-size: 20pt ;");
		 yAxis.setStyle(" -fx-font-size: 20pt ;");
		 if(isSimple==true){
			scatterChart.setTitle(chartTitle);
			createSimple();
		}
		else{
			scatterChart.setTitle(chartTitle);
			createComplex();
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
	 public void createSimple(){
		 try{
			xAxis.setLabel(xAxisOpt.get(0));
			yAxis.setLabel(yAxisOpt.get(0));
			if(seriesOpt.size()==1){
				scatterChart.setLegendVisible(false);
			}
			for(int i=0;i<seriesOpt.size();i++){
				series = new XYChart.Series<>();
				series.setName(seriesOpt.get(i));
				ArrayList<String> xResults = new ArrayList<String>();
				ArrayList<String> yResults = new ArrayList<String>();
				String update="use "+projects.get(i);
	        	updater = new guiRequestManager.UpdateManager(update);
				if(!xTables.get(i).equals(yTables.get(i))){
					String query="select "+xAxisOpt.get(i)+" from "+xTables.get(i);
					provider = new guiRequestManager.DataProvider(query);
					xResults=provider.executeQuery();
					query="select "+yAxisOpt.get(i)+" from "+yTables.get(i);
					provider = new guiRequestManager.DataProvider(query);
					yResults=provider.executeQuery();
				}
				else{
					ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
					ArrayList<String> fields = new ArrayList<String>();
						fields.add(xAxisOpt.get(i));
						fields.add(yAxisOpt.get(i));
						provider = new guiRequestManager.DataProvider(xTables.get(i), 2, null, fields);
						allResults=provider.selectSome();
						xResults=allResults.get(0);
						yResults=allResults.get(1);
				}
				if(xResults.size()!=yResults.size()){
					String warning="Invalid ScatterPlot!xAxis column is not the same size as yAxis!";
					valid=false;
					showWarning(warning);
				}
				else{
					for(int j=0;j<xResults.size();j++){
						if(!xResults.get(j).equals(" ") && !yResults.get(j).equals(" ")){
							series.getData().add(new XYChart.Data<>(Double.parseDouble(xResults.get(j)), Double.parseDouble(yResults.get(j))));
						}
					}
					scatterChart.getData().add(series);
				}
			}
		 }
		 catch(Exception e){
			 String warning="Invalid Chart!";
			 showWarning(warning);
		 }
	}
	public void createComplex(){
		try{
			xAxis.setLabel(xAxisOpt.get(0));
			yAxis.setLabel(yAxisOpt.get(0));
			ArrayList<String> result = new ArrayList<String>();
			String query="select "+seriesCol+" from "+currentSTable;
			provider = new guiRequestManager.DataProvider(query);
			result=provider.executeQuery();
			HashMap<String,ArrayList<Integer>> positions = new HashMap<String,ArrayList<Integer>>();
			for(int i=0;i<result.size();i++){
				if(!distinctValues.contains(result.get(i))){
					distinctValues.add(result.get(i));
					ArrayList<Integer> temp = new ArrayList<Integer>();
					positions.put(result.get(i), temp);
					positions.get(result.get(i)).add(i);
				}
				else{
					positions.get(result.get(i)).add(i);
				}
			}
			ArrayList<String> allXResults = new ArrayList<String>();
			ArrayList<String> allYResults = new ArrayList<String>();
			if(!currentXTable.equals(currentYTable)){
				String query1="select "+xAxisOpt.get(0)+" from "+currentXTable;
				provider = new guiRequestManager.DataProvider(query1);
				allXResults=provider.executeQuery();
				query1="select "+yAxisOpt.get(0)+" from "+currentYTable;
				provider = new guiRequestManager.DataProvider(query1);
				allYResults=provider.executeQuery();
			}
			else{
				ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
				ArrayList<String> fields = new ArrayList<String>();
					fields.add(xAxisOpt.get(0));
					fields.add(yAxisOpt.get(0));
					provider = new guiRequestManager.DataProvider(currentXTable, 2, null, fields);
					allResults=provider.selectSome();
					allXResults=allResults.get(0);
					allYResults=allResults.get(1);
			}
			for(int i=0;i<distinctValues.size();i++){
				series = new XYChart.Series<>();
				series.setName(distinctValues.get(i));
				ArrayList<String> xResults = new ArrayList<String>();
				ArrayList<String> yResults = new ArrayList<String>();
				String update="use "+projects.get(0);
	        	updater = new guiRequestManager.UpdateManager(update);
				for(int j=0;j<positions.get(distinctValues.get(i)).size();j++){
					xResults.add(allXResults.get(positions.get(distinctValues.get(i)).get(j)));
					yResults.add(allYResults.get(positions.get(distinctValues.get(i)).get(j)));
				}
				if(xResults.size()!=yResults.size()){
					String warning="Invalid ScatterPlot!xAxis column is not the same size as yAxis!";
					valid=false;
					showWarning(warning);
				}
				else{
					for(int j=0;j<xResults.size();j++){
						if(!xResults.get(j).equals(" ") && !yResults.get(j).equals(" ")){
							series.getData().add(new XYChart.Data<>(Double.parseDouble(xResults.get(j)), Double.parseDouble(yResults.get(j))));
						}
					}
					scatterChart.getData().add(series);
				}
			}
		}
		catch(Exception e){
			 String warning="Invalid Chart!";
			 showWarning(warning);
		 }
	}
	public void mainScatter(){
		try{
			ArrayList<Node> seriesNodes = new ArrayList<Node>();
			xAxis.setLabel(xAx);
			yAxis.setLabel(yAx);
			xAxis.tickLabelFontProperty().set(Font.font(15));
			yAxis.tickLabelFontProperty().set(Font.font(15));
			HashMap<String,ArrayList<Integer>> positions = new HashMap<String,ArrayList<Integer>>();
			if(seriesColumn.size()!=0){
				for(int i=0;i<seriesColumn.size();i++){
					if(!distinctValues.contains(seriesColumn.get(i))){
						distinctValues.add(seriesColumn.get(i));
						ArrayList<Integer> temp = new ArrayList<Integer>();
						positions.put(seriesColumn.get(i), temp);
						positions.get(seriesColumn.get(i)).add(i);
					}
					else{
						positions.get(seriesColumn.get(i)).add(i);
					}
				}
				try{
					FXMLLoader loader = new FXMLLoader();
			        loader.setLocation((getClass().getResource("/application/SeriesChoice.fxml")));
			        AnchorPane page = (AnchorPane) loader.load();
			        Stage dialogStage = new Stage();
			        Scene scene = new Scene(page);
			        dialogStage.setScene(scene);
			        SeriesChoiceController  sCC= loader.getController();
			        sCC.initialize(distinctValues);
			        dialogStage.showAndWait();
			        if(sCC.getDefault()==false){
			        	seriesFormats=sCC.getFormats();
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			else{
				distinctValues.add("series");
				scatterChart.setLegendVisible(false);
			}
			for(int i=0;i<distinctValues.size();i++){
				series = new XYChart.Series<>();
				series.setName(distinctValues.get(i));
				ArrayList<String> xResults = new ArrayList<String>();
				ArrayList<String> yResults = new ArrayList<String>();
				String update="use "+curDb;
	        	updater = new guiRequestManager.UpdateManager(update);
				if(!ser.equals("")){
					for(int j=0;j<positions.get(distinctValues.get(i)).size();j++){
						xResults.add(xValues.get(positions.get(distinctValues.get(i)).get(j)));
						yResults.add(yValues.get(positions.get(distinctValues.get(i)).get(j)));
					}
				}
				else{
					xResults=xValues;
					yResults=yValues;
				}
				if(xResults.size()!=yResults.size()){
					String warning="Invalid ScatterPlot!xAxis column is not the same size as yAxis!";
					valid=false;
					showWarning(warning);
				}
				else{
					for(int j=0;j<xResults.size();j++){
						if(!xResults.get(j).equals(" ") && !yResults.get(j).equals(" ")){
							try{
								series.getData().add(new XYChart.Data<>(Double.parseDouble(xResults.get(j)), Double.parseDouble(yResults.get(j))));
							}
							catch(Exception e){}
						}	
					}	
					scatterChart.getData().add(series);
					String seriesStyle=".series";
					seriesStyle+=i;
					if(seriesFormats.size()==0){
						Set<Node> nodes = scatterChart.lookupAll(seriesStyle); 
					    for (Node n : nodes) {
					        n.setStyle("-fx-blend-mode: multiply ;");
					    }
					}
					else{
						int pos=0;
						for(int k=0;k<seriesFormats.size();k++){
							if(distinctValues.get(i).equals(seriesFormats.get(k).getValue())){
								pos=k;
								break;
							}
						}
						Set<Node> nodes = scatterChart.lookupAll(seriesStyle); 
						int flag=0;
						for (Node n : nodes) {
							n.setStyle("-fx-blend-mode: multiply ; "+seriesFormats.get(pos).getShape()+" "+seriesFormats.get(pos).getColour());
							 if(flag==0){
								 seriesNodes.add(n);
							 }
							 flag++;
						}
				   	series.setName(seriesFormats.get(pos).getName());
					}
				}	
			}
			if(seriesFormats.size()!=0){
				 Set<Node> items = scatterChart.lookupAll("Label.chart-legend-item");
				 int it=0;
				 for (Node item : items) {
				      Label label = (Label) item;
				      label.setGraphic(seriesNodes.get(it));
				      it++;
				 }
			}
		}
		catch(Exception e){
			 String warning="Invalid Chart!";
			 showWarning(warning);
		 }
	}	
	public void saveChart(ActionEvent event){
		  WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);
		  	String imgName= new String();
		  	if(!chartTitle.equals("")){
		  		imgName=chartTitle+".png";
		  	}
		  	else{
		  		imgName="ScatterChart.png";
		  	}
		  	String filePath="charts/"+imgName;
		    File file = new File(filePath);
		    try {
		        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		    } catch (IOException e){}
	}
}