package org.cs27x.dropbox;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileStates;

public class DropboxProtocol {

	private final DropboxTransport transport_;
	
	private final DropboxCmdProcessor cmdProcessor_;

	private final Map<Kind<?>, OpCode> eventMap;

	public DropboxProtocol(DropboxTransport transport, FileStates states, FileManager filemgr) {
		transport_ = transport;
		cmdProcessor_ = new DropboxCmdProcessor(states,filemgr);
		transport_.addListener(cmdProcessor_);
		
	
		
		eventMap = new HashMap<>();
		eventMap.put(ENTRY_CREATE, OpCode.ADD);
		eventMap.put(ENTRY_MODIFY, OpCode.UPDATE);
		eventMap.put(ENTRY_DELETE, OpCode.REMOVE);
	}

	public void connect(String initialPeer) {
		transport_.connect(initialPeer);
	}

	public void publish(DropboxCmd cmd) {
		transport_.publish(cmd);
	}
	
	public DropboxCmd getDropboxCmd(FileEvent evt){
		DropboxCmd cmd = new DropboxCmd();
		cmd.setPath(evt.getFile().getFileName().toString());
		cmd.setOpCode(eventMap.get(evt.getEventType()));
		
		// If this method isn't equipped to handle the Kind
		// (ie ADD, UPDATE, or REMOVE, then return null.
		if (cmd.getOpCode() == null) return null;
		
		if (cmd.getOpCode() == OpCode.ADD || cmd.getOpCode() == OpCode.UPDATE){
			try {
	
				try (InputStream in = Files.newInputStream(evt.getFile())) {
					byte[] data = IOUtils.toByteArray(in);
					cmd.setData(data);
				}
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cmd;
	}


}
