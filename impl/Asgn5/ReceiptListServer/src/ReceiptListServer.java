import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class ReceiptListServer
{
	private static final int DEFAULT_PORT = 80;
	private static final String ALL_RECEIPTS_PATH = "/user";
	private static final String SYNC_RECEIPTS_PATH = "/sync";

	public static void main(String[] args) throws Exception
	{
		InetSocketAddress address = new InetSocketAddress(DEFAULT_PORT);
		HttpServer httpServer;
		try
		{
			httpServer = HttpServer.create(address, 0);
			httpServer.createContext(ALL_RECEIPTS_PATH, new AllReceiptsHandler());
			httpServer.createContext(SYNC_RECEIPTS_PATH, new SyncReceiptsHandler());
			httpServer.setExecutor(Executors.newCachedThreadPool());
			httpServer.start();
			System.out.println("Server started and listening on port " + DEFAULT_PORT);
		}
		catch (IOException e)
		{
			System.out.println("Error starting server");
		}
	}
}
