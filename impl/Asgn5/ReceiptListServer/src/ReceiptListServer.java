import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ReceiptListServer {
	
	private static final int RECEIPT_DATA = 1;
		public static void main(String[] args) throws Exception{
			System.out.println("Waiting for connection");
			ServerSocket mServerSocket = new ServerSocket(8080);
			Socket mSocket = mServerSocket.accept();

			
			
			InputStream in = mSocket.getInputStream();
			
			// Get the client number and data type out of the first 2 bytes of the transmission
			int appNumber = in.read();
			int dataType = in.read();
			BackupGenerator backupGenerator = null;
			switch(dataType){
				case RECEIPT_DATA:
					backupGenerator = new BackupGeneratorReceiptList();
					break;
				default:
					System.out.println("Client: " + appNumber);
					System.out.println("Type: " + dataType);
					in.close();
					mServerSocket.close();
					mSocket.close();
					throw new UnsupportedOperationException();
			}
			
			backupGenerator.processStream(in, appNumber);
			
			// Cleanup
			in.close();
			mSocket.close();
			mServerSocket.close();
		}
}
