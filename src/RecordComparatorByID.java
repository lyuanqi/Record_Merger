import java.util.Comparator;

//this class creates a comparator for Record object
//with ascending order sorted by the recors's ID
public class RecordComparatorByID implements Comparator<Record>{

	@Override
	public int compare(Record r1, Record r2) {
		return r1.getID()-r2.getID();
	}
}