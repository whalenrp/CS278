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

    @Override
    public FileState getState(Path p){
        return new FileState(777,FileTime.fromMillis(777));
    }

    @Override
    public FileState getOrCreateState(Path p){
        return new FileState(777,FileTime.fromMillis(777));
    }

    @Override
    public FileState insert(Path p) throws IOException{
        return new FileState(777,FileTime.fromMillis(777));
    }

    @Override
    public FileEvent filter(FileEvent evt) throws IOException{
        return evt;
    }
}
