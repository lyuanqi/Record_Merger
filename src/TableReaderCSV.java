import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;

//this class read an CSV file to create a RecordTable object
public class TableReaderCSV extends TableReader {

	public TableReaderCSV(Path path){
		super(path);
	}
	
	public RecordTable getTable(String encoding){
		if (!isValidFile(path)){
			try {
				throw new Exception("the target input file is not a valid file: " + path.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		RecordTable table = new RecordTable();
		boolean hasSetHeaders=false;
		int IDcol=-1;
		
		//read CSV line by line with CSVReader
        CSVReader reader=null;
		try {
			reader = new CSVReader(new InputStreamReader(
			        new FileInputStream(path.toString()), encoding));
        
		String [] nextLine;

		while ((nextLine = reader.readNext()) != null) {
			if(!hasSetHeaders){
				for (int i=0;i<nextLine.length;i++){
					// the line is identified as the header if any identifier of IDs is found in that line
					if (Arrays.asList(MULTILANGUAGE_IDs).contains(nextLine[i])){
						
						IDcol=i;
						ArrayList<String> headers = new ArrayList<String>();
						
						//set the fields in the line to be headers, the headers do not include ID
						for (int j=0;j<nextLine.length;j++){
							if (j!=i){
								headers.add(nextLine[j]);
							}
						}						
						table.setHeaders(headers);
						hasSetHeaders=true;
						break;
					}
				}
			}
			//create a record for each of the following lines once the headers are set
			else{
				Record current=new Record();
				for (int i=0;i<nextLine.length;i++){
					if(i==IDcol){
						current.setID(Integer.parseInt(nextLine[i]));
					}
					else{
						current.addField(nextLine[i]);
					}
				}
				table.addRecord(current);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return table;
	}
}
