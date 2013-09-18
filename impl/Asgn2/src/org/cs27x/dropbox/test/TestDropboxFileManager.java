package org.cs27x.dropbox.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.cs27x.dropbox.DefaultFileManager;
import org.junit.Test;


public class TestDropboxFileManager {
	private final String outFile = "foo.txt";
	private final String testString = "Hello World! Testing FileManager operations";
	private final String updatedString = "Hello World 2!";
	private DefaultFileManager fileManager = new DefaultFileManager(Paths.get("."));
	
	@Test
	public void testFileOperations() throws IOException{
		// Create a test file to work with.
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
		bos.write(testString.getBytes());
		bos.flush();
		bos.close();
		
		testExists();
		testWrite();
		testDelete();
	}
	
	public void testExists(){
		assertTrue("TestDropboxFileManager : FAILED to find " + outFile + ".", 
				fileManager.exists(Paths.get(outFile)));
	}
	
	public void testWrite() throws IOException{
		fileManager.write(Paths.get(outFile), updatedString.getBytes(), true);
		
		InputStream in = Files.newInputStream(Paths.get(outFile));
		byte[] data = IOUtils.toByteArray(in);
		assertArrayEquals("TestDropboxFileManager : FAILED to write correct output string.",
				updatedString.getBytes(),
				data);
		
	}
	
	public void testDelete() throws IOException{
		fileManager.delete(Paths.get(outFile));
		
		assertFalse("TestDropboxFileManager : FAILED to delete file " + outFile + ".",
				fileManager.exists(Paths.get(outFile)));
	}
	
	@Test
	public void testResolve(){
		fileManager = new DefaultFileManager(Paths.get("src"));
		Path absPath = Paths.get("src").resolve(".");
		assertEquals("TestDropboxFileManager : FAILED to resolve path '.'",
				absPath,
				fileManager.resolve("."));
	}

}
