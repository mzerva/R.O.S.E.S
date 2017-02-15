package chartGUIsManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SeriesChoiceController implements Initializable{
	@FXML
	private Button defaultButton = new Button();
	@FXML
	private Button seriesOptionsButton = new Button();
	private boolean isDefault;
	private ArrayList<String> series = new ArrayList<String>();
	private ArrayList<SeriesFormat> seriesFormats = new ArrayList<SeriesFormat>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	public void initialize(ArrayList<String> series){
		this.series=series;
	}
	
	@FXML
	public void useDefault(ActionEvent event){
		isDefault=true;
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	@FXML
	public void seriesOptions(ActionEvent event){
		isDefault=false;
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation((getClass().getResource("/application/SeriesOptions.fxml")));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        SeriesOptionsController  sOC= loader.getController();
	        sOC.initialize(series);
	        dialogStage.showAndWait();
	        seriesFormats=sOC.getFormats();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	public boolean getDefault(){
		return isDefault;
	}
	public ArrayList<SeriesFormat> getFormats(){
		return seriesFormats;
	}
}
