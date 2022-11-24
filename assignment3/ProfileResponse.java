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
            m7 = new Councilors(localhost, portSet[6], responseProfile[3], false);
            m8 = new Councilors(localhost, portSet[7], responseProfile[3], false);
            m9 = new Councilors(localhost, portSet[8], responseProfile[3], false);
        } else if (Objects.equals(s, "Immediate")) {
            System.out.println("Response time: Immediate");
            m1 = new Councilors(localhost, portSet[0], responseProfile[0], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[0], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[0], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[0], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[0], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[0], false);
            m7 = new Councilors(localhost, portSet[6], responseProfile[0], false);
            m8 = new Councilors(localhost, portSet[7], responseProfile[0], false);
            m9 = new Councilors(localhost, portSet[8], responseProfile[0], false);
        } else if (Objects.equals(s, "Medium")) {
            System.out.println("Response time: Medium");
            m1 = new Councilors(localhost, portSet[0], responseProfile[4], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[4], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[4], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[4], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[4], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[4], false);
            m7 = new Councilors(localhost, portSet[6], responseProfile[4], false);
            m8 = new Councilors(localhost, portSet[7], responseProfile[4], false);
            m9 = new Councilors(localhost, portSet[8], responseProfile[4], false);
        } else if (Objects.equals(s, "Late")) {
            System.out.println("Response time: Late");
            m1 = new Councilors(localhost, portSet[0], responseProfile[5], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[5], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[5], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[5], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[5], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[5], false);
            m7 = new Councilors(localhost, portSet[6], responseProfile[5], false);
            m8 = new Councilors(localhost, portSet[7], responseProfile[5], false);
            m9 = new Councilors(localhost, portSet[8], responseProfile[5], false);
        } else if (Objects.equals(s, "Never")) {
            System.out.println("Response time: Never");
            m1 = new Councilors(localhost, portSet[0], responseProfile[6], true);
            m2 = new Councilors(localhost, portSet[1], responseProfile[6], true);
            m3 = new Councilors(localhost, portSet[2], responseProfile[6], true);
            m4 = new Councilors(localhost, portSet[3], responseProfile[6], false);
            m5 = new Councilors(localhost, portSet[4], responseProfile[6], false);
            m6 = new Councilors(localhost, portSet[5], responseProfile[6], false);
            m7 = new Councilors(localhost, portSet[6], responseProfile[6], false);
            m8 = new Councilors(localhost, portSet[7], responseProfile[6], false);
            m9 = new Councilors(localhost, portSet[8], responseProfile[6], false);
        }

    }

    public static void main(String[] args) {
        ProfileResponse profileResponse = new ProfileResponse();

        try {
            createCouncilors(args[0]);
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
            Thread.sleep(5000);
            profileResponse.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
