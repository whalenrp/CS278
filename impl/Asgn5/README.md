
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
application from start to finish, however, I have provided an image file from my emulator which contains
an entry of dummy test data. To use it, copy "userdata-qemu.img" from this directory to your emulator's
data directory (typically found at ~/.android/avd on OSX, Linux or C:\Users\<user>\.android\ and then
into the specific emulator's directory you want to use). First, start the server by running "vagrant up"
in this directory and then load the ReceiptList app in the android emulator. Pull the navigation drawer
out from the left side of the screen and click "Export to Server." This will forward the data from 
the emulator through your localhost and into the virtual machine for storage. 
