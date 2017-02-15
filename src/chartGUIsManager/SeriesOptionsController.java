package chartGUIsManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import warningGUIsManager.WarningsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SeriesOptionsController implements Initializable{
	
	@FXML
	private ChoiceBox seriesChoiceBox = new ChoiceBox();
	@FXML
	private ChoiceBox shapeChoiceBox = new ChoiceBox();
	@FXML
	private ChoiceBox colourChoiceBox = new ChoiceBox();
	@FXML
	private TextField nameTextField = new TextField();
	@FXML
	private Button saveButton = new Button();
	@FXML
	private Button deleteChoiceButton = new Button();
	@FXML
	private Button doneButton = new Button();
	@FXML
	private ListView seriesListView = new ListView();
	private ObservableList<String> seriesItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> seriesModel;
	private ObservableList<String> shapesItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> shapesModel;
	private ObservableList<String> coloursItems = FXCollections.observableArrayList();
	private SingleSelectionModel<String> coloursModel;
	private MultipleSelectionModel<String> listViewModel;
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	private ArrayList<String> shapes = new ArrayList<String>();
	private ArrayList<String> colours = new ArrayList<String>();
	private ArrayList<SeriesFormat> seriesFormats = new ArrayList<SeriesFormat>();
	private ObservableList<String> formatChoicesItems = FXCollections.observableArrayList();
	private String curVal= new String();
	private String curName= new String();
	private String curShape= new String();
	private String curColour= new String();
	
	@Override
	public void initialize(URL location, ResourceBundle resources){}
	public void initialize(ArrayList<String> series){
		seriesModel=seriesChoiceBox.getSelectionModel();
		shapesModel=shapeChoiceBox.getSelectionModel();
		coloursModel=colourChoiceBox.getSelectionModel();
		for(int i=0;i<series.size();i++){
			seriesItems.add(series.get(i));
		}
		shapesItems.add("Triangle");
		shapesItems.add("Square");
		shapesItems.add("Diamond");
		shapesItems.add("X");
		coloursItems.add("Red");
		coloursItems.add("Green");
		coloursItems.add("Light Blue");
		coloursItems.add("Dark Blue");
		coloursItems.add("Yellow");
		coloursItems.add("Orange");
		coloursItems.add("Purple");
		coloursItems.add("Grey");
		seriesChoiceBox.setItems(seriesItems);
		shapeChoiceBox.setItems(shapesItems);
		colourChoiceBox.setItems(coloursItems);

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
	@FXML
	public void saveSeries(ActionEvent event){
		if(values.contains(seriesModel.getSelectedItem().toString())){
			showGenWarning("You already set this series!");
			seriesModel.clearSelection();
			shapesModel.clearSelection();
			coloursModel.clearSelection();
			nameTextField.setText("");
		}
		else if(seriesModel.getSelectedIndex()!=-1 && shapesModel.getSelectedIndex()!=-1 && coloursModel.getSelectedIndex()!=-1){
			curVal=seriesModel.getSelectedItem().toString();
			curName=nameTextField.getText();
			curShape=shapesModel.getSelectedItem().toString();
			curColour=coloursModel.getSelectedItem().toString();
			String item= new String();
			item="Series: "+curVal+", Name: "+curName+", Shape: "+curShape+", Colour: "+curColour;
			formatChoicesItems.add(item);
			values.add(curVal);
			shapes.add(curShape);
			colours.add(curColour);
			names.add(curName);
			seriesListView.setItems(formatChoicesItems);
			seriesModel.clearSelection();
			shapesModel.clearSelection();
			coloursModel.clearSelection();
			nameTextField.setText("");
		}
		else{
			showGenWarning("You have to set all categories!");
		}
	}
	@FXML
	public void deleteChoice(ActionEvent event){
		listViewModel=seriesListView.getSelectionModel();
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for(int i=0;i<listViewModel.getSelectedIndices().size();i++){
			selected.add(listViewModel.getSelectedIndices().get(i));
			values.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			names.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			shapes.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
			colours.remove((int)(listViewModel.getSelectedIndices().get(i))-i);
		}
		for(int i=0;i<selected.size();i++){
			formatChoicesItems.remove((int)(selected.get(i))-i);
		}
		seriesListView.setItems(formatChoicesItems);
		listViewModel.clearSelection();
	}
	@FXML
	public void donePressed(ActionEvent event){
		if(seriesItems.size()==shapes.size()){
			for(int i=0;i<shapes.size();i++){
				SeriesFormat sF = new SeriesFormat(values.get(i), names.get(i), shapes.get(i), colours.get(i));
				seriesFormats.add(sF);
			}
		}
		else{
			showGenWarning("You have to set all series!");
		}
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	public ArrayList<SeriesFormat> getFormats(){
		return seriesFormats;
	}

}
