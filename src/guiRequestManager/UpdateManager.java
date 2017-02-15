package guiRequestManager;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateManager {
	private String update;
	private String filepath;
	private String database;
	private String inputFile=null;
	
	public UpdateManager(String update){
		this.update=update;
		execute();
	}
	public UpdateManager(String database, String filepath) throws IOException, SQLException{
		this.database=database;
		this.filepath=filepath;
		executeSQL();
	}
	public UpdateManager(String database, String filepath, String inputFile) throws IOException, SQLException{
		this.database=database;
		this.filepath=filepath;
		this.inputFile=inputFile;
		executeSQL();
	}
	public void execute(){
		mainInfoManager.MainInfo.sqlEngine.executeUpdate(update);
	}
	public void executeSQL() throws IOException, SQLException{
		mainInfoManager.MainInfo.sqlEngine.executeSql(database, filepath, inputFile);
	}
}
