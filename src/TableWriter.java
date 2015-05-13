import java.nio.file.Files;
import java.nio.file.Path;


//this class writes a RecordTable to a file
public abstract class TableWriter{
	protected Path path;
	public TableWriter(Path path){
		this.path=path;
	}

	//path is valid if the parent directory exists and is a valid directory
	public static boolean isValidPath(Path path){
		if (!Files.exists(path.getParent())){
			return false;
		}
		if (!Files.isDirectory(path.getParent())){
			return false;
		}
		return true;
	}
	
	//filter out unwanted formatting or characters from field data
	public static String fieldFiltering(String s){
		
		String filtered=s;
		
		//replace &nbsp; with space
		filtered = s.replace("\u00a0", " ");
		
		//replace multiple space with single space
		filtered=filtered.replaceAll("[ ]+", " ");

		return filtered;

	}
	
	//pass encoding as an argument to allow writing with different possible encodings
	public abstract void writeTable(RecordTable t, String encoding);
}
