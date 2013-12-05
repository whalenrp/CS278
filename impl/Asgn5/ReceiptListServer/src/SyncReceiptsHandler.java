
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SyncReceiptsHandler implements HttpHandler
{
	private ObjectMapper objMapper = new ObjectMapper();

	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		String method = exchange.getRequestMethod();
		if (method.equals("POST"))
		{
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/html");
			exchange.sendResponseHeaders(200, 0);
			
			String req = getStringFromInputStream(exchange.getRequestBody());
			System.out.println(req);
			
			@SuppressWarnings("unchecked")
			Map<String,Object> userData = objMapper.readValue(req, Map.class);
			
			String username = (String)userData.get("username");
			String password = (String)userData.get("password");
			userData.remove("username");
			userData.remove("password");
			
	        IAccountsWrapper accounts = (IAccountsWrapper)JythonBackend.createAccountsWrapper().createObject();
	        
	        // May already be present, add just in case
	        accounts.add_account(username, password);
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> receipts = (List<Map<String, Object>>)userData.get("receipts");
			
			JythonObjectFactory receiptsFactory = JythonBackend.getReceiptFactory();
			
			// will act as a 'dirty bit' that becomes false if a receipt couldn't be added
			boolean allAdded = true;
			for (Map<String, Object> receipt : receipts)
			{
				// No sure if correct order is maintained, so doing it manually.
				String[] keys = {"_id", "title", "amount", "filename", "category", "kind", "date"};
				Object[] values = new Object[keys.length];
				for (int i = 0; i < values.length; i++)
				{
					values[i] = receipt.get(keys[i]);
				}
				
				IReceipt newReceipt = (IReceipt)receiptsFactory.createObject(values, keys);
				boolean added = accounts.add_receipt(username, password, newReceipt);
				if (added == false)
				{
					allAdded = false;
					System.out.println("Receipt with id " + values[0] + "wasn't added!");
				}
			}
			
			Map<String, Boolean> resp = new LinkedHashMap<String, Boolean>();
			resp.put("success", allAdded); // for now...
			
			String rtn = objMapper.writeValueAsString(resp);
			System.out.println("response: " + rtn);
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(rtn.getBytes());
			responseBody.close();
		}
	}
	
	private static String getStringFromInputStream(InputStream is)
	{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		String returnString = null;
		if (scanner.hasNext())
		{
			returnString = scanner.next();
		}
		scanner.close();
		
		return returnString;
	}
}
