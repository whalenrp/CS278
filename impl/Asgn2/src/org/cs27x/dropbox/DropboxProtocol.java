package org.cs27x.dropbox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileStates;

public class DropboxProtocol {

	private final DropboxTransport transport_;
	
	private final DropboxCmdProcessor cmdProcessor_;

	public DropboxProtocol(DropboxTransport transport, FileStates states, FileManager filemgr) {
		transport_ = transport;
		cmdProcessor_ = new DropboxCmdProcessor(states,filemgr);
		transport_.addListener(cmdProcessor_);
	}

	public void connect(String initialPeer) {
		transport_.connect(initialPeer);
	}

	public void publish(DropboxCmd cmd) {
		transport_.publish(cmd);
	}

	public void addFile(Path p) {
		publish(getDropboxCmd(p,OpCode.ADD));
	}

	public void removeFile(Path p) {
		publish(getDropboxCmd(p,OpCode.REMOVE));
	}

	public void updateFile(Path p) {
		publish(getDropboxCmd(p,OpCode.UPDATE));
	}
	
	private DropboxCmd getDropboxCmd(Path p, OpCode op){
		DropboxCmd cmd = new DropboxCmd();
		cmd.setOpCode(OpCode.ADD);
		cmd.setPath(p.getFileName().toString());
		
		if (op == OpCode.ADD || op == OpCode.UPDATE){
			try {
	
				try (InputStream in = Files.newInputStream(p)) {
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
