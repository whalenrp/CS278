import java.util.List;


public interface IAccountsWrapper {
	
	public boolean add_account(String username, String password);
	
	public List<IReceipt> retrieve_all_receipts(String username, String password);
	
	public boolean add_receipt(String username, String password, IReceipt receipt);
	
}
