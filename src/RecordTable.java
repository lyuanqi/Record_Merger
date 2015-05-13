import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RecordTable {

	private ArrayList<String> headers;
	private ArrayList<Record> records;
	
	public RecordTable(){
		headers=new ArrayList<String>();
		records=new ArrayList<Record>();
	}
	
	public void setHeaders(ArrayList<String> headers){
		this.headers.clear();
		this.headers.addAll(headers);
	}
	
	public ArrayList<String> getHeaders(){
		return headers;
	}
	
	public ArrayList<String> getHeadersWithID(){
		ArrayList<String> headersWithID = new ArrayList<String>();
		headersWithID.add("ID");
		headersWithID.addAll(headers);
		return headersWithID;
	}
	
	//add record to a table
	public void addRecord(Record record) throws Exception{
		if (record.getFieldCount()!=headers.size()){
			//throws exception when the field count does not match header count
			throw new Exception(
						"record field count(" +record.getFieldCount() + 
						") does not match the RecordTable's header count(" + headers.size() +")"
					);
		}
		if (record.getID()==-1){
			//throws exception when the record ID is not properly set
			throw new Exception("record ID is not properly set: ID=" + record.getID());
		}
		records.add(record);
	}
	
	public ArrayList<Record> getTableRecords(){
		return records;
	}
	
	public Record getTableRecordByIndex(int index){
		return records.get(index);
	}
	
	public int getRowCount(){
		return records.size();
	}
	
	public void printHeaders(){
		System.out.printf("|");
		System.out.printf("%-20s %-1s", "ID", "|");
		
		for (String header : headers){
			System.out.printf("%-20s %-1s", header, "|");
		}
		System.out.printf("\n");
	}
	
	//sort table with the predefined comparator
	public void sortTable(Comparator<Record> myComparator){
		Collections.sort(records, myComparator);
	}
	
	//for rows with the same ID, keep only one in the table
	public void removeDulplicates(){
		HashMap<Integer, Record> filtered = new HashMap<Integer,Record>();
		for (Record r : records){
			filtered.put(r.getID(), r);
		}
		records.clear();
        for(int key: filtered.keySet()){
        	records.add(filtered.get(key));
        }
	}
	
	public boolean isEmpty(){
		return records.size()==0;
	}
	
	public void printTable(){
		
		if (isEmpty()){
			System.out.println("------------------------EMPTY TABLE--------------------------");
		}
		else{
			printHeaders();
			for (Record r : records){
				r.printRecord();
			}
		}
		System.out.print("========================================================================");
		System.out.println("======================================================================");
	}
}
