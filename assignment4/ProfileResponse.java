import java.util.Objects;

public class ProfileResponse extends VotingExecution{
    protected static void createCouncilors(String s) {
        if (Objects.equals(s, "Default")){
            System.out.println("Response time: Default, as mentioned in requirement");
            m1 = new Councilors(localhost, portSet[0], responseProfile[0], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[1], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[2], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[3], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[3], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[3], false);
//            m7 = new Councilors(localhost, portSet[6], responseProfile[3], false);
//            m8 = new Councilors(localhost, portSet[7], responseProfile[3], false);
//            m9 = new Councilors(localhost, portSet[8], responseProfile[3], false);
//            m10 = new Councilors(localhost, portSet[9], responseProfile[3], false);
//            m11 = new Councilors(localhost, portSet[10], responseProfile[3], false);
//            m12 = new Councilors(localhost, portSet[11], responseProfile[3], false);
//            m13 = new Councilors(localhost, portSet[12], responseProfile[3], false);
//            m14 = new Councilors(localhost, portSet[13], responseProfile[3], false);
//            m15 = new Councilors(localhost, portSet[14], responseProfile[3], false);
//            m16 = new Councilors(localhost, portSet[15], responseProfile[3], true);
//            m17 = new Councilors(localhost, portSet[16], responseProfile[3], true);
//            m18 = new Councilors(localhost, portSet[17], responseProfile[3], true);
//            m19 = new Councilors(localhost, portSet[18], responseProfile[3], false);
//            m20 = new Councilors(localhost, portSet[19], responseProfile[3], false);
//            m21 = new Councilors(localhost, portSet[20], responseProfile[3], false);
//            m22 = new Councilors(localhost, portSet[21], responseProfile[3], false);
//            m23 = new Councilors(localhost, portSet[22], responseProfile[3], false);
//            m24 = new Councilors(localhost, portSet[23], responseProfile[3], false);
//            m25 = new Councilors(localhost, portSet[24], responseProfile[3], false);
//            m26 = new Councilors(localhost, portSet[25], responseProfile[3], false);
//            m27 = new Councilors(localhost, portSet[26], responseProfile[3], false);
//            m28 = new Councilors(localhost, portSet[27], responseProfile[3], false);
//            m29 = new Councilors(localhost, portSet[28], responseProfile[3], false);
//            m30 = new Councilors(localhost, portSet[29], responseProfile[3], false);
//            m31 = new Councilors(localhost, portSet[30], responseProfile[3], false);
//            m32 = new Councilors(localhost, portSet[31], responseProfile[3], false);
//            m33 = new Councilors(localhost, portSet[32], responseProfile[3], false);
//            m34 = new Councilors(localhost, portSet[33], responseProfile[3], false);
//            m35 = new Councilors(localhost, portSet[34], responseProfile[3], false);
//            m36 = new Councilors(localhost, portSet[35], responseProfile[3], false);
//            m37 = new Councilors(localhost, portSet[36], responseProfile[3], false);
//            m38 = new Councilors(localhost, portSet[37], responseProfile[3], false);
//            m39 = new Councilors(localhost, portSet[38], responseProfile[3], false);
//            m40 = new Councilors(localhost, portSet[39], responseProfile[3], false);
        } else if (Objects.equals(s, "Immediate")) {
            System.out.println("Response time: Immediate");
            m1 = new Councilors(localhost, portSet[0], responseProfile[0], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[0], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[0], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[0], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[0], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[0], false);
//            m7 = new Councilors(localhost, portSet[6], responseProfile[0], false);
//            m8 = new Councilors(localhost, portSet[7], responseProfile[0], false);
//            m9 = new Councilors(localhost, portSet[8], responseProfile[0], false);
//            m10 = new Councilors(localhost, portSet[9], responseProfile[0], false);
//            m11 = new Councilors(localhost, portSet[10], responseProfile[0], false);
//            m12 = new Councilors(localhost, portSet[11], responseProfile[0], false);
//            m13 = new Councilors(localhost, portSet[12], responseProfile[0], false);
//            m14 = new Councilors(localhost, portSet[13], responseProfile[0], false);
//            m15 = new Councilors(localhost, portSet[14], responseProfile[0], false);
//            m16 = new Councilors(localhost, portSet[15], responseProfile[0], true);
//            m17 = new Councilors(localhost, portSet[16], responseProfile[0], true);
//            m18 = new Councilors(localhost, portSet[17], responseProfile[0], true);
//            m19 = new Councilors(localhost, portSet[18], responseProfile[0], false);
//            m20 = new Councilors(localhost, portSet[19], responseProfile[0], false);
//            m21 = new Councilors(localhost, portSet[20], responseProfile[0], false);
//            m22 = new Councilors(localhost, portSet[21], responseProfile[0], false);
//            m23 = new Councilors(localhost, portSet[22], responseProfile[0], false);
//            m24 = new Councilors(localhost, portSet[23], responseProfile[0], false);
//            m25 = new Councilors(localhost, portSet[24], responseProfile[0], false);
//            m26 = new Councilors(localhost, portSet[25], responseProfile[0], false);
//            m27 = new Councilors(localhost, portSet[26], responseProfile[0], false);
//            m28 = new Councilors(localhost, portSet[27], responseProfile[0], false);
//            m29 = new Councilors(localhost, portSet[28], responseProfile[0], false);
//            m30 = new Councilors(localhost, portSet[29], responseProfile[0], false);
//            m31 = new Councilors(localhost, portSet[30], responseProfile[0], false);
//            m32 = new Councilors(localhost, portSet[31], responseProfile[0], false);
//            m33 = new Councilors(localhost, portSet[32], responseProfile[0], false);
//            m34 = new Councilors(localhost, portSet[33], responseProfile[0], false);
//            m35 = new Councilors(localhost, portSet[34], responseProfile[0], false);
//            m36 = new Councilors(localhost, portSet[35], responseProfile[0], false);
//            m37 = new Councilors(localhost, portSet[36], responseProfile[0], false);
//            m38 = new Councilors(localhost, portSet[37], responseProfile[0], false);
//            m39 = new Councilors(localhost, portSet[38], responseProfile[0], false);
//            m40 = new Councilors(localhost, portSet[39], responseProfile[0], false);
        }

    }

    public static long runTest(String args) {
        ProfileResponse profileResponse = new ProfileResponse();
        long startTime=System.currentTimeMillis();

        try {
            createCouncilors(args);
            startServer();
            Thread.sleep(500);
//            profileResponse.voting(m1);
//            profileResponse.voting(m2);
//            profileResponse.voting(m3);
            new Thread(new Voting(proposerN, endInfo, m1, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m2, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m3, portSet)).start();

            while (VotingExecution.endInfo.get() != 0) {
                Thread.sleep(100);
            }
//            Thread.sleep(5000);
            profileResponse.end();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime=System.currentTimeMillis();
//        System.out.println("Runtime： "+(endTime-startTime)+"ms");
        return endTime-startTime;
    }
}
