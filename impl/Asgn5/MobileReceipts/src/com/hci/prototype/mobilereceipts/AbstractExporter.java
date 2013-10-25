package com.hci.prototype.mobilereceipts;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*
 * This class defines how to connect to the server and send the contents of the 
 * database query over the networking using TCP. 
 */
public abstract class AbstractExporter {
	
	private Context mContext;
	
	public AbstractExporter(Context ctxt){
		mContext = ctxt;
	}
	/*
	 * Implements the template pattern by connecting to the given server and
	 * calling the derived class' particular method of formatting 
	 */
	public final void connectAndSendData(String dstName, int dstPort) {
		Socket outSocket = null;
		OutputStream outStream = null;
		try {
			outSocket = new Socket(dstName, dstPort);
			outStream = outSocket.getOutputStream();
			
			byte[] mByteData = formatData();
			
			outStream.write(mByteData);
			
		} catch (UnknownHostException e) {
			Toast.makeText(mContext.getApplicationContext(), "Could not locate host. Please try again.", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.e("AbstractExporter", "IOException thrown.", e);
		} finally {
			try{
				if (outStream == null){
					Toast.makeText(mContext.getApplicationContext(), "Could not locate host. Please try again.", Toast.LENGTH_SHORT).show();
					return;
				}
				outStream.close();
				outSocket.close();} catch(IOException e){};
		}
		
	}
	
	/*
	 * Abstract function usedy by derived classes to retrieve and format
	 * data for backup transmission over the network.
	 */
	protected abstract byte[] formatData();
}
