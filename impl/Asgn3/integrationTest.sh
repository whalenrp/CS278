
#!/bin/bash

export SAVE_CLASSPATH=$CLASSPATH
export CLASSPATH=$CLASSPATH:bin:libs:src:libs/hazelcast-all-2.3.1.jar:libs/commons-io-2.4.jar
SERVER_LOG=server-output.log
CLIENT_LOG=client-output.log
SERVER_DIR=test-server
CLIENT_DIR=test-client
CONTROL_DIR=test-control


###########################
# Functions
###########################

# waitForOutputFromLog takes the log file as a parameter
# and will block until a line containing the string "STARTED"
# is appended to the log.
function waitForOutputFromLog {
    { tail -n 0 -f $1 & } | sed -n "/STARTED/q"
}

# Takes care of initializing the environment for the integration test.
# - Initializes the server and blocks until it completes
# - Grabs the IP address from the log file (more cross-platform than using
#    built-in tools like ipconfig or ifconfig, but dependent on Hazelcast log formatting)
# - Initializes the client with the IP_ADDR from the previous step.
# - Creates testing directories
function setUp {
    touch $SERVER_LOG
    touch $CLIENT_LOG

    # Start the server in the background, outputting to a log file
    java org.cs27x.dropbox.Dropbox test-server &>> $SERVER_LOG &
    echo "Waiting for server to initialize..."
    waitForOutputFromLog $SERVER_LOG 
    echo "Server initialized"

    # This disgusting regex grabs the IP address from the line containing "STARTED" in the server output log
    # This depends on the log file formatting the IP address as "[IP_ADDR]" on the line with "STARTED"
    IP_ADDR=$(tail -10 server-output.log | sed -n '/STARTED/p' | sed 's/[][]/-/g' | sed 's/.*-\([0-9\.]*\)-.*/\1/')

    java org.cs27x.dropbox.Dropbox test-client $IP_ADDR &>> $CLIENT_LOG &
    echo "Waiting for client to initialize..."
    waitForOutputFromLog $CLIENT_LOG
    echo "Client initialized"

    mkdir $CLIENT_DIR
    mkdir $SERVER_DIR
    mkdir $CONTROL_DIR
}

# Recursively deletes testing files
function tearDown {
    rm -rf $CLIENT_DIR
    rm -rf $SERVER_DIR
    rm -rf $CONTROL_DIR
    rm $CLIENT_LOG
    rm $SERVER_LOG
}

# Takes a command as a string parameter and the time (in seconds) to wait
# for it to complete. 
# This string will be executed and, after the delay, the function will 
# recursively check for equality between the two directories, $SERVER_DIR
# and $CLIENT_DIR and compare them to a control directory which 
# contains the correct structure, just in case something is deleted.
function assertDirEquals {
    eval $1
    sleep $2
    
    # If the client directory != server dir or client != control
    # directory, or the directory with the correct values, then
    # we have failed
    if [[ -z $(diff -bqr $SERVER_DIR $CLIENT_DIR) &&
          -z $(diff -bqr $CLIENT_DIR $CONTROL_DIR) ]]; then
        echo "PASSED : $1"
    else
        echo "FAILED : $1"
    fi
    return 0
}

###########################
# End Functions
###########################

# Create teardown so we don't leave dropbox running in the background
# This will be executed either on forced exit or normal exit
trap 'kill $(jobs -p);tearDown' EXIT
#trap 'kill $(jobs -p)' EXIT

set -e
# Start server and client for testing
setUp

#Begin integration tests
touch $CLIENT_DIR/primer.txt
rm $CLIENT_DIR/primer.txt

# Test Adding, Updating and Removing from client to server,
# making a "control group" in $CONTROL_DIR that serves as 
# our template.
echo 'Hello, test' >  $CONTROL_DIR/echo_test.txt
assertDirEquals "echo 'Hello, test' >  $CLIENT_DIR/echo_test.txt" 1

echo 'Hello, test2' >  $CONTROL_DIR/echo_test2.txt
assertDirEquals "echo 'Hello, test2' >  $CLIENT_DIR/echo_test2.txt" 1

echo 'update1' >>  $CONTROL_DIR/echo_test.txt
assertDirEquals "echo 'update1' >>  $CLIENT_DIR/echo_test.txt" 1

echo 'update2' >>  $CONTROL_DIR/echo_test2.txt
assertDirEquals "echo 'update2' >>  $CLIENT_DIR/echo_test2.txt" 1

rm $CONTROL_DIR/echo_test.txt
assertDirEquals "rm $CLIENT_DIR/echo_test.txt" 1

rm $CONTROL_DIR/echo_test2.txt
assertDirEquals "rm $CLIENT_DIR/echo_test2.txt" 1


# Test Adding, Removing and Updating from server to client
echo 'Hello, test' >  $CONTROL_DIR/echo_test.txt
assertDirEquals "echo 'Hello, test' >  $SERVER_DIR/echo_test.txt" 1

echo 'Hello, test2' >  $CONTROL_DIR/echo_test2.txt
assertDirEquals "echo 'Hello, test2' >  $SERVER_DIR/echo_test2.txt" 1

echo 'update1' >>  $CONTROL_DIR/echo_test.txt
assertDirEquals "echo 'update1' >>  $SERVER_DIR/echo_test.txt" 1

echo 'update2' >>  $CONTROL_DIR/echo_test2.txt
assertDirEquals "echo 'update2' >>  $SERVER_DIR/echo_test2.txt" 1

rm $CONTROL_DIR/echo_test.txt
assertDirEquals "rm $SERVER_DIR/echo_test.txt" 1

rm $CONTROL_DIR/echo_test2.txt
assertDirEquals "rm $SERVER_DIR/echo_test2.txt" 1

# Undo classpath changes before exiting
export CLASSPATH=SAVE_CLASSPATH
