package org.cs27x.dropbox.test;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Paths;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.dropbox.DropboxProtocol;
import org.cs27x.dropbox.FileManager;
import org.cs27x.filewatcher.DropboxFileEventHandler;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;

public class TestDropboxFileEventHandler {
	private final String filename = "foo.txt";

	@Test
	public void testHandler() throws IOException{
		// Initialize variables
		FileManager mgr = mock(FileManager.class);
		FileStates states = mock(FileStates.class);
		DropboxProtocol protocol = mock(DropboxProtocol.class);
		
		DropboxCmd cmd = new DropboxCmd();
		cmd.setOpCode(OpCode.ADD);
		cmd.setPath(filename);
		FileEvent evt = new FileEvent(ENTRY_CREATE, Paths.get(filename));
		
		// Set up callbacks and create EventHandler
		when(mgr.resolve(any(String.class))).thenReturn(Paths.get(filename));
		when(protocol.getDropboxCmd(any(FileEvent.class))).thenReturn(cmd);
		when(states.filter(any(FileEvent.class))).thenReturn(evt);
		DropboxFileEventHandler handler = new DropboxFileEventHandler(mgr, states,protocol);
		
		// Perform testing
		handler.handle(evt);
		verify(states).filter(any(FileEvent.class));
		verify(protocol).publish(cmd);
	}
}
