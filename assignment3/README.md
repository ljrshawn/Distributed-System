**COMP-SCI-7076 Distributed Systems**

**Assignment2**

by Jingran Shawn Lyu(a1832252)

JDK version: JDK16

**This code implement the following function:**

* Implementation works when two or three councillors send voting proposals at the same time.
* Implementation works in the case where all M1-M9 have immediate responses to voting queries.
* Implementation works when M1 – M9 have responses to voting queries suggested by the profiles above, 
  including when M2 or M3 propose and then go offline.
* Implementation works with a number ‘n’ of councillors with four profiles of response times: 
  immediate; medium; late; never

**Totally contain 7 .java files:**

Councilors.java (for implement paxos method)
Proposal.java (for implement proposal object)
VotingExecution.java (for implement voting process)
ImmediateResponse.java (for implement voting as immediate response)
ProfileResponse.java (for implement voting as default, immediate, medium, late, and never response)
ProposeNOffline.java (for implement voting as M2 or M3 offline)
CompareLeader.java (for compare the result of every member)


**Test Instructions**

There 7 test case:

1. Three councillors send voting proposals at the same time
   sh TestThreeVoteImme.sh

2. M1-M9 have immediate responses to voting queries
   sh TestImmediateRes.sh

3. M1 – M9 have responses to voting queries suggested by the profiles above
   sh TestProfilesVote.sh

4. M2 or M3 propose and then go offline
   sh TestM2orM3offline.sh

5. M1-M9 have medium responses to voting queries
   sh TestMediumRes.sh

6. M1-M9 have late responses to voting queries
   sh TestLateRes.sh

7. M1-M9 have never responses to voting queries
   sh TestNeverRes.sh

8. Manual operate *.java
   1) javac *.java
   2) i.   java ProfileResponse Configuration(should be Default, Immediate, Medium, Late, Never)
      ii.  java ImmediateResponse
      iii. java ProposeNOffline