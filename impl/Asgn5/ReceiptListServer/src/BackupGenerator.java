import java.io.IOException;
import java.io.InputStream;

/*
 * Abstract class defining the interface for creating a backup log of a 
 * client application from an InputStream.
 */
public abstract class BackupGenerator {
	
	/*
	 * Receives an InputStream containing the state of client application's
	 * data.
	 * @param is Holds the state of a client's application.
	 * @param clientNumber The unique id for the user of this data.
	 */
	public abstract void processStream(InputStream is, int clientNumber) throws IOException;

}
