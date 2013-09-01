package org.cs27x.dropbox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import org.cs27x.dropbox.DefaultFileManager;
import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.dropbox.DropboxCmdProcessor;
import org.cs27x.dropbox.FileManager;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileState;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;

/**
 * Please give your tests rational names that are related to what you are
 * testing!
 * 
 * @author jules
 * 
 */
public class TestDropboxCmdProcessor {

	/**
	 * Helpful hints:
	 * 
	 * 1. JUnit provides a bunch of helpful assertion methods for checking
	 * conditions, such as:
	 * 
	 * assertNotNull(...) assertTrue(...) assertEquals(expected, actual)
	 * 
	 * You can import these assertions into a test by adding the following
	 * static import:
	 * 
	 * import static org.junit.Assert.*;
	 * 
	 * 2. Mockito provides a bunch of helpful methods for verifying calls to
	 * mock objects, such as:
	 * 
	 * verify(some_mock_obj,
	 * the_number_of_expected_calls).theMethodYouExpectedWasCalled(
	 * eq(the_value_you_wanted_for_param1), eq(the_value_you_wanted_for_param2)
	 * );
	 * 
	 * You can import these helpers with:
	 * 
	 * import static org.mockito.Mockito.*;
	 * 
	 * @throws Exception
	 */

	// Testing cmdProcessor with mock FileManager and FileStates objects.
	// Step 2 of exercise.
	@Test
	public void testDropboxCmdProcessorWithMock() throws IOException{
		FileManager mgr = mock(FileManager.class);
		FileStates states = mock(FileStates.class);
		FileState state  = new FileState(0,FileTime.fromMillis(0));
		when(states.getState(any(Path.class))).thenReturn(state);
		when(states.getOrCreateState(any(Path.class))).thenReturn(state);
		//FileStates states = new FileStatesImpl();
		//states.insert(Paths.get("./"));
		DropboxCmdProcessor processor = new DropboxCmdProcessor(states, mgr);
		
		DropboxCmd cmdAdd = new DropboxCmd();
		byte[] data = new String("Helo, world").getBytes();
		cmdAdd.setData(data);
		cmdAdd.setOpCode(OpCode.ADD);
		cmdAdd.setPath("foo");
		
		processor.updateFileState(cmdAdd,Paths.get("."));
		
		assertEquals("State sizes unequal after call to updateFileState",state.getSize(), data.length);
		assertFalse("Last Modified time for state incorrectly set after call to DropboxCmdProcessor.updateFileState",
				state.getLastModificationDate() == FileTime.fromMillis(0));
		
		
//		DropboxCmd cmdDel = new DropboxCmd();
//		cmdDel.setOpCode(OpCode.ADD);
//		cmdDel.setPath("foo");
//		
//		processor.cmdReceived(cmdDel);
	}

	// @Mock
	// Path mockPath;
	@Test
	public void testDropboxCmdProcessorFileStates() {

		// Test the file removed condition
		Path mockPath = Paths.get("./");
		FileStateTestImpl testImpl = new FileStateTestImpl();
		DropboxCmdProcessor processor = new DropboxCmdProcessor(testImpl,
				new DefaultFileManager(mockPath));

		DropboxCmd cmd = mock(DropboxCmd.class);
		when(cmd.getOpCode()).thenReturn(OpCode.REMOVE);
		processor.updateFileState(cmd, mockPath);
		assertEquals(testImpl.getState(mockPath).getSize(), -1);

		// Test the file created/updated condition
		when(cmd.getOpCode()).thenReturn(OpCode.ADD);
		when(cmd.getData()).thenReturn(new byte[1000]);
		processor.updateFileState(cmd,mockPath);
		assertEquals(testImpl.getState(mockPath).getSize(), 1000);
	}

	public class FileManagerStub implements FileManager {

		private final FileTime modificationTime_;

		public FileManagerStub(FileTime modtime) {
			modificationTime_ = modtime;
		}

		@Override
		public void write(Path p, byte[] data, boolean overwrite)
				throws IOException {
		}

		@Override
		public Path resolve(String relativePathName) {
			return null;
		}

		@Override
		public boolean exists(Path p) {
			return false;
		}

		// @Override
		// public FileTime getLastModifiedTime(Path p) throws IOException {
		// return modificationTime_;
		// }

		@Override
		public void delete(Path p) throws IOException {
		}
	}

	public class FileStatesStub implements FileStates {

		private final FileState state_;

		public FileStatesStub(FileState state) {
			state_ = state;
		}

		@Override
		public FileState getState(Path p) {
			return state_;
		}

		@Override
		public FileState getOrCreateState(Path p) {
			return getState(p);
		}

		@Override
		public FileState insert(Path p) throws IOException {
			return null;
		}

		@Override
		public FileEvent filter(FileEvent evt) throws IOException {
			return null;
		}
	}

	public class DropboxCmdProcessorTest {

		@Test
		public void testRemoveWithStub() {

			final FileState state = new FileState(0, FileTime.fromMillis(0));

			DropboxCmdProcessor proc = new DropboxCmdProcessor(
					new FileStatesStub(state), new FileManagerStub(
							FileTime.fromMillis(0)));

			DropboxCmd cmd = new DropboxCmd();
			cmd.setOpCode(OpCode.REMOVE);
			cmd.setPath("foo");
			proc.updateFileState(cmd, Paths.get("foo"));

			assertEquals(-1, state.getSize());
		}

		 @Test
		 public void testAddUpdateMock() throws Exception {
		
		 final FileState state = new FileState(0, FileTime.fromMillis(0));
		 //final FileTime changeTime = FileTime.fromMillis(101);
		
		 FileStates states = mock(FileStates.class);
		 FileManager mgr = mock(FileManager.class);
		 //when(mgr.getLastModifiedTime(any(Path.class))).thenReturn(changeTime);
		 when(states.getOrCreateState(any(Path.class))).thenReturn(state);
		 when(states.getState(any(Path.class))).thenReturn(state);
		
		 DropboxCmdProcessor proc = new DropboxCmdProcessor(states, mgr);
		
		 DropboxCmd cmd = new DropboxCmd();
		 cmd.setOpCode(OpCode.ADD);
		 cmd.setPath("foo");
		 byte[] data = new byte[1011];
		 cmd.setData(data);
		 proc.updateFileState(cmd, Paths.get("foo"));
		
		 //assertEquals(changeTime, state.getLastModificationDate());
		 assertEquals(data.length, state.getSize());
		
		 state.setLastModificationDate(FileTime.fromMillis(1));
		 state.setSize(0);
		
		 cmd.setOpCode(OpCode.UPDATE);
		 cmd.setPath("foo");
		 cmd.setData(data);
		 proc.updateFileState(cmd, Paths.get("foo"));
		
		 //assertEquals(changeTime, state.getLastModificationDate());
		 assertEquals(data.length, state.getSize());
		 }

		@Test
		public void testAddUpdateWithStub() {

			final FileState state = new FileState(0, FileTime.fromMillis(0));
			final FileTime changeTime = FileTime.fromMillis(101);

			FileStates states = new FileStatesStub(state);
			FileManager mgr = new FileManagerStub(changeTime);

			DropboxCmdProcessor proc = new DropboxCmdProcessor(states, mgr);

			DropboxCmd cmd = new DropboxCmd();
			cmd.setOpCode(OpCode.ADD);
			cmd.setPath("foo");
			byte[] data = new byte[1011];
			cmd.setData(data);
			proc.updateFileState(cmd, Paths.get("foo"));

			assertEquals(changeTime, state.getLastModificationDate());
			assertEquals(data.length, state.getSize());

			state.setLastModificationDate(FileTime.fromMillis(1));
			state.setSize(0);

			cmd.setOpCode(OpCode.UPDATE);
			cmd.setPath("foo");
			cmd.setData(data);
			proc.updateFileState(cmd, Paths.get("foo"));

			assertEquals(changeTime, state.getLastModificationDate());
			assertEquals(data.length, state.getSize());
		}

	}
}
