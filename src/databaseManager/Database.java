package databaseManager;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
 
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
 
public class Database  {
	private static Connection con;
	private static String filePath;
	private static String inputFile= new String();
	private static boolean files2=false;
	private static String file1=new String();
	private static String file2=new String();
    public Database(Connection con, String filePath){
    	this.con=con;
    	this.filePath=filePath;
    }
    public Database(Connection con, String filePath, String inputFile){
    	this.con=con;
    	this.filePath=filePath;
    	this.inputFile=inputFile;
    	if(inputFile.contains("&")){
    		files2=true;
    		String tokens[]=inputFile.split("&");
    		file1=tokens[0];
    		file2=tokens[1];
    	}
    	else{
    		files2=false;
    	}
    }
    public void execute() throws SQLException{
        String s= new String();
        StringBuffer sb = new StringBuffer();
        try{
            FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);
            int count=0;
            while((s = br.readLine()) != null){
            	if(s.contains("infile")){
            		String[] tokens=s.split("infile");
            		if(files2==false){
            			s=tokens[0]+" infile '"+inputFile.replace("\\","\\\\")+"' "+tokens[1];
            		}
            		else{
            			if(count==0){
                			s=tokens[0]+" infile '"+file1+"' "+tokens[1];
                			count++;
            			}
            			else{
                			s=tokens[0]+" infile '"+file2+"' "+tokens[1];
            			}
            		}
            	}
                sb.append(s);
            }
            br.close();
            //Ta .sql prepei na exoun keno prin to ; gia na mhn uparxei provlhma me ta csv
            String[] inst = sb.toString().split(" ;");
            Statement st = con.createStatement();
            for(int i = 0; i<inst.length; i++){
                if(!inst[i].trim().equals("")){
                    st.executeUpdate(inst[i]);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("*** Error : "+e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
        
    }
}
