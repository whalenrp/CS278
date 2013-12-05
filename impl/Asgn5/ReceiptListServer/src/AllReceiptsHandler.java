import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AllReceiptsHandler implements HttpHandler
{

	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		String method = exchange.getRequestMethod();
		if (method.equals("GET"))
		{
			System.out.println("GET /user");
			String response = "";
			
			String parameters = exchange.getRequestURI().getQuery();
			
			Map<String, String> map = getParameters(parameters);
			System.out.println("username=" + map.get("username") + "\npassword=" + map.get("password"));
			
			JythonObjectFactory accountsFactory = JythonBackend.createAccountsWrapper();
			
	        IAccountsWrapper accounts = (IAccountsWrapper) accountsFactory.createObject();
			
			// check if params are ok
	        
			if (map.containsKey("username") && map.containsKey("password"))
			{
				List<IReceipt> receipts = accounts.retrieve_all_receipts(map.get("username"), map.get("password"));
				if (receipts.size() > 0)
				{
					String tmp = null;
					try
					{
						tmp = generateHTML(receipts);
					}
					catch (JsonProcessingException e)
					{
						tmp = "Error marshalling back to JSON!";
					}
					
					response += tmp;
				}
				else
				{
					response += "Either the list is empty, the user doesn't exist, or the password is wrong.";
				}
			}
			else
			{
				response += "Invalid params!";
			}
			
			System.out.println(response);
			
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/html");
			exchange.sendResponseHeaders(200, 0);
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(response.getBytes());
			responseBody.close();
			
			System.out.println("Finished GET processing");
		}
	}
	// Horrible, horrible things happening here	
	private String generateHTML(List<IReceipt> receipts) throws JsonProcessingException
	{
		String rtn = "<html>";
		rtn += "<img src=\"http://i.minus.com/ixI2b731j8hDx.png\" algt=\"banner\">\n";
		for (IReceipt r : receipts)
		{
			rtn += buildReceiptHTML(r);
			rtn += "<p>\n";
		}
		
		rtn += "</html>";
		
		return rtn;
	}
	
	private String buildReceiptHTML(IReceipt rec)
	{
		String rtn = "";
		Map<String, String> data = rec.serialize();
		
		rtn += "<h4>" + data.get("title") + "</h4>\n";
		data.remove("title");
		
		rtn += "<ul>\n";
		for (String key : data.keySet())
		{
			rtn += "<li><b>" + key + "</b> = " + data.get(key) + "</li>\n";
		}
		rtn += "</ul>\n";
		
		return rtn;
	}
	
	private Map<String, String> getParameters(String query)
	{
		Map<String, String> map = new HashMap<String, String>();
		for (String param : query.split("&"))
		{
			String pair[] = param.split("=");
			if (pair.length > 1)
			{
				map.put(pair[0], pair[1]);
			}
			else
			{
				map.put(pair[0], "");
			}
		}
		
		return map;
	}
}
