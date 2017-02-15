package databaseManager;
import java.io.IOException;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mainInfoManager.MainInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;


public class CentralSQLEngine {
    private ArrayList<String> result;
    
    public void executeUpdate(String update){
    	PreparedStatement statement; 
    	try{
            statement = (PreparedStatement) MainInfo.con.prepareStatement(update); 
            statement.executeUpdate();
       }
       catch(SQLException ex){}
    }
    public void executeSql(String db, String filepath, String inputFile) throws IOException,SQLException {
    	String update="use "+db;
    	executeUpdate(update);
    	Database dB;
    	if(inputFile==null){
    		dB= new Database(MainInfo.con, filepath);
    	}
    	else{
       		dB= new Database(MainInfo.con, filepath, inputFile);
    	}
    	dB.execute();
   }
    public ArrayList<String> executeQuery(String query){
    	result=new ArrayList<String>();
     	ResultSet res;
     	Statement state;
     	 try{
     		 state=MainInfo.con.createStatement();
             res=(ResultSet) state.executeQuery(query);
             while(res.next()){
            	 result.add(res.getString(1));
             }
             state.close();
         }
         catch(SQLException ex)
         {
        	 System.out.println(ex.getMessage());
         }
     	 return result;
    }
    
    public ArrayList<String> selectFrom(String table, String field, String orderBy){
    	ArrayList<String> stringResult= new ArrayList<String>();
    	Statement state;
        ResultSet res;
        String query="select "+field+" from "+table+" order by "+orderBy;
        try{
            state=MainInfo.con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            while(res.next()){
           		stringResult.add(res.getString(field));
            	
            }
            state.close();
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        return stringResult;
    }
    public ArrayList<ArrayList<String>> selectAllFrom(String table,int col, String orderBy){
    	ArrayList<ArrayList<String>> stringResult= new ArrayList<ArrayList<String>>();
    	for(int i=0;i<col;i++){
    		ArrayList<String> temp = new ArrayList<String>();
    		stringResult.add(temp);
    	}
    	Statement state;
        ResultSet res;
        String query="select * from "+table+" order by "+orderBy;
        try{
            state=MainInfo.con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            while(res.next()){
            	for(int i=1;i<=col;i++){
            		stringResult.get(i-1).add(res.getString(i));
            	}
            }
            state.close();
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        return stringResult;
    }
    public ArrayList<ArrayList<String>> selectSomeFrom(String table,int col, String where, ArrayList<String> fields){
    	ArrayList<ArrayList<String>> stringResult= new ArrayList<ArrayList<String>>();
    	for(int i=0;i<col;i++){
    		ArrayList<String> temp = new ArrayList<String>();
    		stringResult.add(temp);
    	}
    	Statement state;
        ResultSet res;
        String query="select ";
        for(int i=0;i<fields.size();i++){
        	if(i==0){
        		query+=fields.get(i);
        	}
        	else{
        		query+=" , "+fields.get(i);
        	}
        }
        query+=" from "+table;
        if(where!=null){
        	query+=" where "+where;
        }
        try{
            state=MainInfo.con.createStatement();
            res=(ResultSet) state.executeQuery(query);
            while(res.next()){
            	for(int i=1;i<=col;i++){
            		stringResult.get(i-1).add(res.getString(i));
            	}
            }
            state.close();
        }
        catch(SQLException ex)
        {
        	 System.out.println(ex.getMessage());
        }
        return stringResult;
    }  
}