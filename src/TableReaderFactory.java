import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;


public class TableReaderFactory {
	private Path path;
	public TableReaderFactory(Path path){
		this.path=path;
	}
	public TableReader getReader(){
		String fileType=FilenameUtils.getExtension(path.toString());
		if(fileType.equalsIgnoreCase("html")){
			//System.out.println("Created a html raeder");
			return new TableReaderHTML(path);
		} 
		else if(fileType.equalsIgnoreCase("csv")){
			//System.out.println("Created a csv raeder");
			return new TableReaderCSV(path);
		}
	      
		return null;
	}
}
