package mainGUIManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileConverter {
	private String path;
	private Scanner inputReader;
	private PrintWriter outputWriter = null;
	private String output;
	private PrintWriter outputWriter1 = null;
	private String output1;
	private File file1;
	private File file2;
	private int change=0;
	public FileConverter(String path, int change){
		this.path=path;
		this.change=change;
		File file = new File(path);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",path); 
		 }
	}
	public void process(){
		int lines=0;
		int tables=0;
		int version=0;
		output=path+".txt";
		file1 = new File(output);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(output)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
		while(inputReader.hasNextLine()){
			String[] tokens;
			String line=inputReader.nextLine();	
			tokens=line.split(";");
			if(lines>0){
				for(int j=1;j<tokens.length;j++){	
					if(change!=0){
						outputWriter.println(tables+";"+version+";"+change+";"+tokens[j]);
						version++;
					}
					else{
						outputWriter.println(tables+";"+version+";"+tokens[j]);
						version++;
					}
				}
			}
			tables++;
			version=0;
			lines++;
		}
		outputWriter.close();
		inputReader.close();
	}
	public void processAll(){
		int lines=0;
		int tables=0;
		int version=0;
		output=path+".txt";
		file1 = new File(output);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(output)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output);
		 }
		output1=path+"1.txt";
		file2 = new File(output1);
		try 
		 { 
			outputWriter1 = new PrintWriter(new FileOutputStream(output1)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.txt.\n",output1);
		 }
		while(inputReader.hasNextLine()){
			String[] tokens;
			String line=inputReader.nextLine();	
			tokens=line.split(";");
			if(lines>0){
				for(int j=1;j<tokens.length;j++){	
					String[] tokens1;
					tokens1=tokens[j].split("[|\\[\\]]");
					outputWriter1.println(tables+";"+version+";"+tokens1[0]);
					outputWriter.println(tables+";"+version+";"+1+";"+tokens1[1]);
					outputWriter.println(tables+";"+version+";"+2+";"+tokens1[2]);
					outputWriter.println(tables+";"+version+";"+3+";"+tokens1[3]);
					outputWriter.println(tables+";"+version+";"+4+";"+tokens1[4]);
					version++;
				}
			}
			tables++;
			version=0;
			lines++;
		}
		outputWriter1.close();
		outputWriter.close();
		inputReader.close();
	}
	public File getFile1(){
		return file1;
	}
	public File getFile2(){
		return file2;
	}
}
