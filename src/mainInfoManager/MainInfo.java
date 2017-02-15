package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class StaticStuff {
	public static HashMap<String,String> files = new HashMap<String,String>();
	/*Path gia to arxeio .sql dhmiourgias ths vashs*/
	public static String filePath="db_config/DBEvolution.sql";
	private static String url = "jdbc:mysql://localhost:3306/?user=EvolutionUser&password=Evolution";
	/*user k password ths vashs sthn opoia theloume na sundethoume */
    public static Connection con;
    public static String warning = new String();
	public static databaseManager.CentralSQLEngine sqlEngine;
	public static final void initialize(){
		sqlEngine= new databaseManager.CentralSQLEngine();
	
		/*Zeugh (arxeio, arxeio antistoixou load)*/
		
		String CurrentCmdFolder = "inputData/loadCommands/";
		
		files.put("table_del.csv", CurrentCmdFolder+"loadChangesD.sql");
		files.put("table_ins.csv", CurrentCmdFolder+"loadChangesI.sql");
		files.put("table_key.csv", CurrentCmdFolder+"loadChangesK.sql");
		files.put("table_type.csv", CurrentCmdFolder+"loadChangesT.sql");
		files.put("tables.csv", CurrentCmdFolder+"loadAttributesT.sql");
		files.put("table_stats.csv", CurrentCmdFolder+"loadStats.sql");
		files.put("metrics.csv", CurrentCmdFolder+"loadMetrics.sql");
		files.put("transitions.csv", CurrentCmdFolder+"loadTransitionEvents.sql");
		files.put("all.csv", CurrentCmdFolder+"loadAll.sql");
	}
	public static void init(){
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		try{
            con = DriverManager.getConnection(url);
       }
       catch(SQLException ex){
            System.err.println("SQLException: "+ex.getMessage());
       }
	}
}
