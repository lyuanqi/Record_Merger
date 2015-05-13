import java.util.ArrayList;

public class Record {
	
	private ArrayList<String> fields;
	private int ID;
	
	public Record(){
		fields=new ArrayList<String>();
		ID=-1;
	}
	
	public void setID(int ID){
		this.ID=ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setFields(String[] inputFields){
		for (int i = 0; i < inputFields.length; i++){
			fields.add(inputFields[i]);
		}
	}
	
	public void addField(String field){
		fields.add(field);
	}
	
	public ArrayList<String> getFields(){
		return fields;
	}
	
	public ArrayList<String> getFieldsWithID(){
		ArrayList<String> fieldsWithID = new ArrayList<String>();
		fieldsWithID.add(Integer.toString(ID));
		fieldsWithID.addAll(fields);
		return fieldsWithID;
	}
	
	public String getFieldAt(int index){
		return fields.get(index);
	}
	
	public void setField(int index, String newValue){
		fields.set(index, newValue);
	}
	
	public int getFieldCount(){
		return fields.size();
	}
	
	public void printRecord(){
		System.out.printf("|");
		System.out.printf("%-20s %-1s", ID, "|");
		
		for (String field : fields){
			System.out.printf("%-20s %-1s", TableWriter.fieldFiltering(field), "|");
		}
		System.out.printf("\n");
	}
}
