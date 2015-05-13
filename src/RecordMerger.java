import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";
	//store the output file to the current directory
	public static final String PATH="./" + FILENAME_COMBINED;
	
	public static RecordTable mergeTwoTables(RecordTable t1, RecordTable t2){
		RecordTable merged = new RecordTable();
		
		ArrayList<String> headers1=t1.getHeaders();
		ArrayList<String> headers2=t2.getHeaders();
		
		//a hash map for each table
		//field name ->(maps to) index in record for the first table
		//index ->(maps to) field name in record for the second table
		HashMap<String,Integer> colMap1 = new HashMap<String,Integer>();
		HashMap<Integer,String> colMap2 = new HashMap<Integer,String>();
		
		//another hashmap is used to map the record ID to the record for the second table
		HashMap<Integer,Record> t2map = new HashMap<Integer,Record>();
		
		//before merging, firstly remove duplicates with same ID in t1 and t2 themselves
		t1.removeDulplicates();
		t2.removeDulplicates();
		
		//store corresponding key-value pairs in each hash map
		for (int i=0;i<headers1.size();i++){
			colMap1.put(headers1.get(i), i);
		}
		for (int i=0;i<headers2.size();i++){
			colMap2.put(i, headers2.get(i));
		}
		for (Record r : t2.getTableRecords()){
			t2map.put(r.getID(), r);
		}
		
		ArrayList<String> mergedHeaders = new ArrayList<String>();
		//store the names of the duplicated fields
		ArrayList<String> duplicateHeaders=new ArrayList<String>();
		//store the corresponding indices of the second table for all the duplicated fields
		ArrayList<Integer> duplicateIndices=new ArrayList<Integer>();

		//add all headers of the first table to the final table
		mergedHeaders.addAll(headers1);


		for (int i=0;i<headers2.size();i++){
			String header = headers2.get(i);
			//if found duplicates
			if(headers1.contains(header)){
				duplicateHeaders.add(header);
				duplicateIndices.add(i);
			}
			else{
				mergedHeaders.add(header);
			}
		}
		
		merged.setHeaders(mergedHeaders);
		
		// for each record from t1, create a record and add all fields to the new record
		for (Record r : t1.getTableRecords()){
			Record mergedRecord = new Record();
			mergedRecord.setID(r.getID());
			
			for (String field : r.getFields()){
				
				mergedRecord.addField(field);
			}
			
			//if a matching record exists in t2 that has the same ID, merge with the new record
			if (t2map.containsKey(r.getID())){
				Record matchedRecord=t2map.get(r.getID());

				//for duplicate columns, concatenate them two fields if they hold different values for the same ID
				//flag such cases with "(conflict)" in the field of the new record
				for (int i=0;i<headers2.size();i++){
					if (duplicateIndices.contains(i)){
						int index = colMap1.get(colMap2.get(i));
						if (!r.getFieldAt(index).equals(matchedRecord.getFieldAt(i))){
							mergedRecord.setField(index, r.getFieldAt(index) + ", " + 
									matchedRecord.getFieldAt(i) + " (conflict)");
						}
					}
					else{
						mergedRecord.addField(matchedRecord.getFieldAt(i));
					}
				}
				//remove the record from the t2map once the matching record is merged into the new record 
				t2map.remove(r.getID());
			}
			//if no matching record exists, append blank fields to the missing columns in the new record
			else{
				for (int i=headers2.size();i<mergedHeaders.size();i++){
					mergedRecord.addField("");
				}
			}
			try {
				//add the new record into the final table
				merged.addRecord(mergedRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//the remaining records in t2map has no matching ID with t1
		//add these records to the final table
		for (int key : t2map.keySet()){
			Record mergedRecord = new Record();
			Record current = t2map.get(key);
			mergedRecord.setID(current.getID());
			
			//for columns with missing fields, append empty string
			for (int i=0;i<headers1.size();i++){
				mergedRecord.addField("");
			}
			//fill the columns with the fields from t2's records
			for (int i=0;i<headers2.size();i++){
				if (duplicateIndices.contains(i)){
					int index = colMap1.get(colMap2.get(i));
					mergedRecord.setField(index, current.getFieldAt(i));
				}
				else{
					mergedRecord.addField(current.getFieldAt(i));
				}
			}
			try {
				//add the new record into the final table
				merged.addRecord(mergedRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//sort table with RecordComparatorByID
		merged.sortTable(new RecordComparatorByID());
		merged.printTable();
		return merged;
	}
	
	public static RecordTable mergeMultipleTables(RecordTable[] tables){
		if (tables.length<1){
			return new RecordTable();
		}
		else if (tables.length==1){
			return tables[0];
		}
		else if (tables.length==2){
			return mergeTwoTables(tables[0],tables[1]);
		}
		else{
			RecordTable merged = mergeTwoTables(tables[0],tables[1]);
			for (int i=2;i<tables.length;i++){
				merged=mergeTwoTables(merged, tables[i]);
			}
			return merged;
		}	
	}
	
	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {
		
		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}
		String temp="ddddss";
		temp=temp.replace("s","f");
		System.out.println(temp);
		
		Path[] paths = new Path[args.length];
		TableReader[] readers = new TableReader[args.length];
		RecordTable[] tables = new RecordTable[args.length];
		
		for (int i=0;i<args.length;i++){
			paths[i] = Paths.get(args[i]);
			readers[i] = new TableReaderFactory(paths[i]).getReader();
			tables[i] = readers[i].getTable("UTF-8");
			tables[i].printTable();
		}
		RecordTable merged = mergeMultipleTables(tables);
		
		TableWriter writer= new TableWriterFactory(Paths.get(PATH)).getWriter();
		writer.writeTable(merged, "UTF-8");
	}
}
