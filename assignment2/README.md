**COMP-SCI-7076 Distributed Systems**

**Assignment2**

by Jingran Shawn Lyu(a1832252)

JDK version: JDK16

**This code implement the following function:**

Basic function:
Text sending works
client, Aggregation server and content server processes start up and communicate
PUT operation works for one(also Multiple) content server
GET operation works for many read clients
Atom server expired feeds works (12s)
Retry on errors (server not available etc.) works

Full functionality refers to:
Lamport clocks are implemented
All error codes are implemented
Content servers are replicated and fault-tolerant

There are 5 .java files to process the operation. They are:
AggregationServer.java(Define the Aggregation Server)
ContentServer.java(Define the Content Server)
GETClient.java(Define the Client)
MyFile.java(Define the format of sending text strings)
CompareTxt.java(Compare the output txt from GETClient and input txt from ContentServer)

**Test Instructions**
There are 5 types test method:
1. BASE
   Please run in three different terminals:
   sh Test_Base_run_server.sh
   sh Test_Base_run_cs.sh
   sh Test_Base_run_client.sh

2. Kill AggregationServer in 20 seconds
   Please run in three different terminals:
   sh Test_ASKill_run_server.sh
   sh Test_ASKill_run_cs.sh
   sh Test_ASKill_run_client.sh

3. Kill ContentServer in 20 seconds
   Please run in three different terminals:
   sh Test_CSKill_run_server.sh
   sh Test_CSKill_run_cs.sh
   sh Test_CSKill_run_client.sh

4. Multiple ContentServer
   Please run in three different terminals:
   sh Test_MultipleCS_run_server.sh
   sh Test_MultipleCS_run_cs.sh
   sh Test_MultipleCS_run_client.sh

5. Multiple Client
   Please run in three different terminals:
   sh Test_MultipleClient_run_server.sh
   sh Test_MultipleClient_run_cs.sh
   sh Test_MultipleClient_run_client.sh

6. The operation of .java files
   javac *.java
   1) AggregationServer.java
      java AggregationServer survival_time[optional](fault = infinity)
      (for example: java AggregationServer 20)
   
   2) ContentServer.java
      java ContentServer server_port file_path time[optional]
      (if time < 1000, it would be survival time, the fault heart beat time would be 5s. 
      if time > 1000, it would be heart beat time, the fault survival time would be infinity.)
      (for example: java ContentServer http://127.0.0.1:4567 (20))

   3) GETClient.java
      java GETClient server_port
      (for example: java GETClient http://127.0.0.1:4567)

   4) MyFile.java
   
   5) CompareTxt.java
      java CompareTxt output_path
      (for example: java CompareTxt output.txt)
      

Thank you for your read. You can try now :D!