package org.cs27x.dropbox.test;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxProtocol;
import org.cs27x.dropbox.DropboxTransport;
import org.cs27x.dropbox.FileManager;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;

public class TestDropboxProtocol {
	private final String testString = "Hello World! This is a test";
	private final String outFile = "foo.txt";
	private Map<Kind<?>, OpCode> eventMap;
	

	@Test
	public void testGetDropboxCmd() throws Exception {
		// Create a test file to compare with the expected result.
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
		bos.write(testString.getBytes());
		bos.flush();
		bos.close();
		
		// Initialize map with FileEvent relations
		eventMap = new HashMap<>();
		eventMap.put(ENTRY_CREATE, OpCode.ADD);
		eventMap.put(ENTRY_MODIFY, OpCode.UPDATE);
		eventMap.put(ENTRY_DELETE, OpCode.REMOVE);
		
		DropboxProtocol protocol = new DropboxProtocol(
				mock(DropboxTransport.class),
				mock(FileStates.class),
				mock(FileManager.class));
		
		testFileEvents(ENTRY_CREATE, protocol);
		testFileEvents(ENTRY_MODIFY, protocol);
		testFileEvents(ENTRY_DELETE, protocol);
	}
	
	private void testFileEvents(Kind<Path> entryCreate, DropboxProtocol protocol){
		DropboxCmd result, expected;
		expected = new DropboxCmd();

		// Test ENTRY_CREATE
		result = protocol.getDropboxCmd(new FileEvent(entryCreate, Paths.get(outFile)));
		expected.setOpCode(eventMap.get(entryCreate));
		expected.setData(testString.getBytes());
		expected.setPath(outFile);
		
		assertEquals("DropboxProtocol : getDropboxCmd for " +  entryCreate.name() +" in invalid OpCode", 
				result.getOpCode(),
				expected.getOpCode());
		assertEquals("DropboxProtocol : getDropboxCmd for " + entryCreate.name() + " results in invalid Path", 
				result.getPath(),
				expected.getPath());
		
		if (expected.getOpCode() == OpCode.ADD || expected.getOpCode() == OpCode.UPDATE){
			assertArrayEquals("DropboxProtocol : getDropboxCmd for " + entryCreate.name() + " results in invalid bytes", 
					result.getData(),
					expected.getData());
		}
	}
	
}
