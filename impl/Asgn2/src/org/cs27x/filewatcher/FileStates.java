package org.cs27x.filewatcher;

import java.io.IOException;
import java.nio.file.Path;
/**
 * Created by richard on 8/29/13.
 */
public interface FileStates {
    public FileState getState(Path p);

    public FileState getOrCreateState(Path p);

    public FileState insert(Path p) throws IOException;



    public FileEvent filter(FileEvent evt) throws IOException;
}
