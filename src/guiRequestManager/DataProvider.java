package guiRequestManager;

import java.util.ArrayList;

public class DataProvider {
	private ArrayList<String> result;
	private ArrayList<ArrayList<String>> results;
	private String query;
	private String table;
	private String field;
	private String orderBy;
	private int col;
	private String where;
	private ArrayList<String> fields;

	public DataProvider(String query){
		this.query=query;
	}
	public DataProvider(String table, String field, String orderBy){
		this.table=table;
		this.field=field;
		this.orderBy=orderBy;
	}
	public DataProvider(String table, int col, String orderBy){
		this.table=table;
		this.col=col;
		this.orderBy=orderBy;
	}
	public DataProvider(String table, int col, String where, ArrayList<String> fields){
		this.table=table;
		this.col=col;
		this.fields=fields;
		this.where=where;
	}
	public ArrayList<String> executeQuery(){
		result=mainInfoManager.MainInfo.sqlEngine.executeQuery(query);
		return result;
	}
	public ArrayList<String> selectFrom(){
		result=mainInfoManager.MainInfo.sqlEngine.selectFrom(table, field, orderBy);
		return result;
	}
	public ArrayList<ArrayList<String>> selectAll(){
		results=mainInfoManager.MainInfo.sqlEngine.selectAllFrom(table, col, orderBy);
		return results;
	}
	public ArrayList<ArrayList<String>> selectSome(){
		results=mainInfoManager.MainInfo.sqlEngine.selectSomeFrom(table, col, where, fields);
		return results;
	}
}
