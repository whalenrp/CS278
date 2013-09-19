import shlex,subprocess,os
import socket

# Open /dev/null to suppress output
FNULL = open(os.devnull, 'w')

# Start the server, keeping a handle to it for cleanup purposes.
args = shlex.split("java -cp 'src;libs/*;bin;' org.cs27x.dropbox.Dropbox test-server")
#procServer = subprocess.Popen(args, stdout=FNULL, stderr=FNULL)
procServer = subprocess.Popen(args)

# Get the non-localhost IP address. 
# Copied from http://stackoverflow.com/questions/166506/finding-local-ip-addresses-using-pythons-stdlib
#print([ip for ip in socket.gethostbyname_ex(socket.gethostname())[2] if not ip.startswith("127.")][:1])


#a=0
#while True:
#    ++a

