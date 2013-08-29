package org.cs27x.dropbox.test;

import java.nio.file.attribute.FileTime;
import org.cs27x.filewatcher.FileState;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileStates;
import java.io.IOException;
import java.nio.file.Path;
/**
 * Created by richard on 8/29/13.
 */
public class FileStateTestImpl implements FileStates{
	private FileState state = new FileState(0,FileTime.fromMillis(777));
    @Override
    public FileState getState(Path p){
        return state;
    }

    @Override
    public FileState getOrCreateState(Path p){
        return state;
    }

    @Override
    public FileState insert(Path p) throws IOException{
        return state;
    }

    @Override
    public FileEvent filter(FileEvent evt) throws IOException{
        return evt;
    }
}
