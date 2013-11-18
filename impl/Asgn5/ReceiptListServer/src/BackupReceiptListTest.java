import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BackupReceiptListTest {
	
	private class MyInputStream extends InputStream{
		byte[] mData = null;
		int lastReadByte = 0;
		public MyInputStream(String testString){
			mData = testString.getBytes();
		}
		
		@Override
		public int read() throws IOException {
			if (lastReadByte == mData.length) return -1;
			return mData[lastReadByte++];
		}
		
	}
	InputStream input = null;

	@Before
	public void setUp() throws Exception {
		input = new MyInputStream("Hello There");
	}

	@After
	public void tearDown() throws Exception {
		input.close();
	}

	@Test
	public void testReceiptBackup() throws IOException {
		BackupGenerator generator = new BackupGeneratorReceiptList();
		generator.processStream(new MyInputStream("Hello, World!"), 1);
		
		Scanner reader = new Scanner(new File("client_1_ReceiptData.txt"));
		String output = reader.nextLine();
		assertEquals(output, "Hello, World!");
		reader.close();
	}
	
	@Test
	public void testJythonFactory(){
		final String PYTHONPATH = "../../mongo-files";
        final String[] sysPathAappends = PYTHONPATH.split(":");
        
        JythonObjectFactory accountsFactory = new JythonObjectFactory(IAccountsWrapper.class,"db_interface","AccountsWrapper",sysPathAappends);
        JythonObjectFactory receiptsFactory = new JythonObjectFactory(IReceipt.class,"db_interface","Receipt",sysPathAappends);
        
        IAccountsWrapper accounts = (IAccountsWrapper) accountsFactory.createObject();
        IReceipt receipt = (IReceipt) receiptsFactory.createObject();
        
        //accounts.add_account("test_user", "test_pass"); currently, no way to clean this up after testing, in java

        List<IReceipt> results = accounts.retrieve_all_receipts("test_user", "test_pass");
        assertEquals(0, results.size());
	}

}
