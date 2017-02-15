package chartGUIsManager;

import java.util.HashMap;

public class SeriesFormat {
	private String value;
	private String name;
	private String shape;
	private String colour;
	private HashMap<String, String> shapes = new HashMap<String, String>();
	private HashMap<String, String> colours = new HashMap<String, String>();
	public SeriesFormat(String value, String name, String shape, String colour){
		shapes.put("Triangle", "-fx-shape: 'M 0 0 v 10 l -5 -5 z';");
		shapes.put("Square", "-fx-shape: 'M 0 0 V 40 H 40 V 0 z';");
		shapes.put("Diamond", "-fx-shape: 'M5,0 L10,9 L5,18 L0,9 Z';");
		shapes.put("X", "-fx-shape: 'M2,0 L5,4 L8,0 L10,0 L10,2 L6,5 L10,8 L10,10 L8,10 L5,6 L2, 10 L0,10 L0,8 L4,5 L0,2 L0,0 Z';");
		colours.put("Red", "-fx-background-color: red;");
		colours.put("Green", "-fx-background-color: green;");
		colours.put("Light Blue", "-fx-background-color: aqua;");
		colours.put("Dark Blue", "-fx-background-color: blue;");
		colours.put("Yellow", "-fx-background-color: yellow;");
		colours.put("Orange", "-fx-background-color: orange;");
		colours.put("Purple", "-fx-background-color: purple;");
		colours.put("Grey", "-fx-background-color: grey;");
		this.value=value;
		this.name=name;
		this.shape=shape;
		this.colour=colour;
	}
	public String getShape(){
		return shapes.get(shape);
	}
	public String getColour(){
		return colours.get(colour);
	}
	public String getName(){
		return name;
	}
	public String getValue(){
		return value;
	}
}
