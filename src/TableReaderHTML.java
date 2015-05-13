import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//this class read an HTML file to create a RecordTable object
public class TableReaderHTML extends TableReader {
	public TableReaderHTML(Path path){
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
		boolean foundHeaders=false;
		int IDcol=-1;
		
		File input = new File(path.toString());
		Document doc=null;
		try {
			doc = Jsoup.parse(input, encoding, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
        Elements tableElements = doc.select("table");
        
        //parse HTML headers
        Elements tableHeaderEles = tableElements.select("tr th");
		ArrayList<String> headers = new ArrayList<String>();
        for (int i = 0; i < tableHeaderEles.size(); i++) {
            String field=tableHeaderEles.get(i).text();
            //find the ID column that matches an identifier in MULTILANGUAGE_IDs
			if (Arrays.asList(MULTILANGUAGE_IDs).contains(field)){
				IDcol=i;
				foundHeaders=true;
			}
			else{
				headers.add(field);
			}
        }
        //create a record for each <tr> once the headers are found
        if (foundHeaders){
    		table.setHeaders(headers);
    		
    		Elements tableRowElements = tableElements.select("tr");
            for (int i = 0; i < tableRowElements.size(); i++) {
            	Element row = tableRowElements.get(i);
            	Elements rowItems = row.select("td");
            	//do not create a record for any <tr> without <td> 
            	if (rowItems.size()==0){
            		continue;
            	}
               
            	//create a record for each valid <tr>
            	Record current = new Record();
               
            	for (int j = 0; j < rowItems.size(); j++) {
            		String field = rowItems.get(j).text();
            		if(j==IDcol){
            			current.setID(Integer.parseInt(field));
            		}
            		else{
            			current.addField(field);
            		}
            	}
            	try {
					table.addRecord(current);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }

		return table;
	}
}
