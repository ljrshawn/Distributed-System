COMP-SCI-7076 Distributed Systems

Assignment1

by Jingran Shawn Lyu(a1832252)

This assignment allows that clients have their own stack on the server. All clients can push numbers or operator into
stack in server and pop or delay pop all the number from server.

There are 5 .java files to process the operation. They are:
Sorter.java(Define the remote interface)
SorterImplementation.java(Define the server implementation),
SorterServerIm.java(Define the server interface to create new stack),
SorterServer.java(Implement the server)), 
and SorterClient.java(Implement the client).

Test Instructions

Please enter following operations in assignment1 terminal:

javac *.java  
rmiregistry &  
java SorterServer &  
java SorterClient case.txt > output.txt &  java SorterClient case1.txt > output1.txt & 
java SorterClient case2.txt > output2.txt &  java SorterClient case3.txt > output3.txt &  
// this can test 4 clients at same time but with own stack

diff EOutput.txt output.txt  
diff EOutput1.txt output1.txt  
diff EOutput2.txt output2.txt  
diff EOutput3.txt output3.txt

SorterClient.java can get the push numbers and operations by reading a .txt file which offered the path.
There is a test case included. The numbers represent the push value. The "ascending", "descending", "max", and "min"
represent the push operator. The pop and delayPop(xxx) represent the pop operator. Once pop, all the number in the 
stack will pop out, and the stack will be empty. After output, the EOutput.txt can compare with to check the result.
Due to pop would print the result and adds a new line, please be very careful when use diff or any other compare 
method.

Thank you for your read. You can try now :D!