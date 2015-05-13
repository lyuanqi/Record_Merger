import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

//this class writes a RecordTable to a CSV file
public class TableWriterCSV extends TableWriter{
	public TableWriterCSV(Path path){
		super(path);
	}

	public void writeTable(RecordTable t, String encoding) {
		if (!isValidPath(path)){
			try {
				throw new Exception("The write path is not valid: " + path.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//uses CSVWriter to write to a CSV file
		CSVWriter writer=null;
		try {
			writer = new CSVWriter(new OutputStreamWriter(
					new FileOutputStream(path.toString()), encoding));
			
			ArrayList<Record> records = t.getTableRecords();
			
			//write headers first
			String[] headers = t.getHeadersWithID().toArray(new String[t.getHeadersWithID().size()]);
			for (int i=0;i<headers.length;i++){
				headers[i] = fieldFiltering(headers[i]);
			}
			writer.writeNext(headers);
			
			//write all other records
			for (Record r : records){
				String[] entries = r.getFieldsWithID().toArray(new String[r.getFieldsWithID().size()]);
				for (int i=0;i<entries.length;i++){
					entries[i] = fieldFiltering(entries[i]);
				}
				writer.writeNext(entries);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
