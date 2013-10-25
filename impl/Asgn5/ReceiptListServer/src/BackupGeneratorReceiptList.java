import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * BackupGeneratorReceiptList defines how to store backups for a given client's 
 * receipt list.
 */
public class BackupGeneratorReceiptList extends BackupGenerator {

	@Override
	public void processStream(InputStream is, int clientNumber) throws IOException {
		OutputStream out = new FileOutputStream("client_" + clientNumber + "_ReceiptData.txt");
		
		System.out.println("Accepted connection from client #" + clientNumber);
		System.out.println(" Writing to file clientData" + clientNumber + ".txt");
		
		// Write bytes to file.
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = is.read(buffer)) != -1){
			try {
				out.write(buffer, 0,  bytesRead);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		out.close();

	}

}
