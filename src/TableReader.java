import java.nio.file.Files;
import java.nio.file.Path;

//this class read a file to create a RecordTable object
public abstract class TableReader{
	//path of input file
	protected Path path;
	//keep track of identifiers of ID because ID header might be in other languages or other wordings
	public static final String[] MULTILANGUAGE_IDs={"ID","编号"};
	
	public TableReader(Path path){
		this.path=path;
	}

	//input file is valid if it exists and readable
	public static boolean isValidFile(Path path){
		if (!Files.exists(path)){
			return false;
		}
		
		if (!Files.isReadable(path)){
			return false;
		}
		
		return true;
	}
	
	// pass encoding as an argument to handle different possible encodings
	public abstract RecordTable getTable(String encoding);
}
