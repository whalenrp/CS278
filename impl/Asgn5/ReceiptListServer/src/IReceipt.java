import java.util.Map;


public interface IReceipt {

	public boolean equals(IReceipt other);
	
	public Map<String,String> serialize();
	
}
