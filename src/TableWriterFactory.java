import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;


public class TableWriterFactory {
	private Path path;
	public TableWriterFactory(Path path){
		this.path=path;
	}
	public TableWriter getWriter(){
		String fileType=FilenameUtils.getExtension(path.toString());
		if(fileType.equalsIgnoreCase("csv")){
			//System.out.println("Created a csv writer");
			return new TableWriterCSV(path);
		}
	      
		return null;
	}
}
