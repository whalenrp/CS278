
This project contains the client and server side portions of an android application that
stores a users receipts along with a picture of them in a database. Ultimately, when a user adds a receipt to
the phone's internal database, an update is triggered where a fresh copy of the data is 
sent to the server for backup. For this assignment, however, the backups are triggered manually
by clicking "Export to Server" in the navigation drawer in the main activity, and the entire
database is being sent over the network, whereas a production-quality build might implement a
way to send only the diffs for a given table. 

