package org.cs27x.dropbox;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.cs27x.filewatcher.DropboxFileEventHandler;
import org.cs27x.filewatcher.FileReactor;
import org.cs27x.filewatcher.FileStates;
import org.cs27x.filewatcher.FileStatesImpl;

public class Dropbox {

	private HazelcastTransport transport_;
	private DropboxProtocol protocol_;
	private FileReactor reactor_;
	
	public Dropbox(Path rootdir){
		reactor_ = new FileReactor(rootdir);
		transport_ = new HazelcastTransport();
		
		FileManager filemgr = new DefaultFileManager(rootdir);
		FileStates states = new FileStatesImpl();
		protocol_ = new DropboxProtocol(transport_, states, filemgr);

		reactor_.addHandler(new DropboxFileEventHandler(filemgr,states,protocol_));
	}
	
	public void connect(String server) throws Exception {
		transport_.connect(server);
		reactor_.start();
	}
	
	public boolean connected(){
		return transport_.isConnected();
	}
	
	public void disconnect(){
		reactor_.stop();
		transport_.disconnect();
	}
	
	public void awaitConnect(long timeout) throws InterruptedException {
		transport_.awaitConnect(timeout);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(args.length);
		
		Dropbox db = new Dropbox(Paths.get(args[0]));
		
		String peer = (args.length > 1)? args[1] : null;
		db.connect(peer);
	}
	
}
