
#!/bin/bash

export SAVE_CLASSPATH=$CLASSPATH
export CLASSPATH=$CLASSPATH:bin:libs:src:libs/hazelcast-all-2.3.1.jar:libs/commons-io-2.4.jar
SERVER_LOG=server-output.log
CLIENT_LOG=client-output.log

###########################
# Functions
###########################

# waitForOutputFromLog takes two parameters, the 
function waitForOutputFromLog {
    { tail -n 0 -f $1 & } | sed -n "/STARTED/q"
}

###########################
# End Functions
###########################


# Start the server in the background, outputting to a log file
java org.cs27x.dropbox.Dropbox test-server &>> $SERVER_LOG &

echo "Waiting.."
waitForOutputFromLog $SERVER_LOG 
echo "Done"
trap "killall background" EXIT
export CLASSPATH=SAVE_CLASSPATH
