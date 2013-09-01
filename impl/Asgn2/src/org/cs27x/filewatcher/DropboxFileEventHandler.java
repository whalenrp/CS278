package org.cs27x.filewatcher;


import java.io.IOException;
import java.nio.file.Path;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxProtocol;
import org.cs27x.dropbox.FileManager;

public class DropboxFileEventHandler implements FileEventHandler {

	private final DropboxProtocol transport_;
	private final FileStates fileStates_;
	private final FileManager fileHandler_;

	public DropboxFileEventHandler(FileManager hdlr, FileStates states,
			DropboxProtocol transport) {
		super();
		fileStates_ = states;
		transport_ = transport;
		fileHandler_ = hdlr;
	
	}

	@Override
	public void handle(FileEvent evt) {
		Path p = evt.getFile();
		p = fileHandler_.resolve(p.getFileName().toString());
		evt = new FileEvent(evt.getEventType(), p);

		try {
			evt = fileStates_.filter(evt);

			if (evt != null) {
				// getDropboxCmd has been modified to return null if the EventType is not one of 
				// ADD, UPDATE, or REMOVE
				DropboxCmd cmd = transport_.getDropboxCmd(evt);
				
				if (cmd != null) transport_.publish(cmd);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
