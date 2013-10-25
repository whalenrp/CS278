
This project contains the client and server side portions of an android application that
stores a users receipts along with a picture of them in a database. Ultimately, when a user adds a receipt to
the phone's internal database, an update is triggered where a fresh copy of the data is 
sent to the server for backup. For this assignment, however, the backups are triggered manually
by clicking "Export to Server" in the navigation drawer in the main activity, and the entire
database is being sent over the network, whereas a production-quality build might implement a
way to send only the diffs for a given table. 

Because the client side of this application is an Android app and emulator setups vary widely across
different computers, I have written only an integration test only for the server side of the application
which tests the format of incoming data (namely, that the first byte represents the customer id and the
second byte is used for the type of data being stored). If you wish to test the functionality of the
application from start to finish, however, I have provided an image file of my emulator's sql database,
which contains a dummy entry for testing purposes. To use it, start up your emulator with eclipse, open
the MobileReceipts application, and
run "adb push data /data/data/com.hci.prototype.mobilereceipts/databases/data" from the command line
(I needed to run this command in cygwin for it to work on Windows).
Once the database has been copied to the emulator, start the virtual machine by running "vagrant up" on 
the command line. It will automatically run the server-side app. To manually trigger a database backup
with the server, pull the navigation drawer out from the left side of the emulator and click "Export to 
Server." It will write the backup data to the vagrant directory.
