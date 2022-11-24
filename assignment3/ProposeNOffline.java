public class ProposeNOffline extends VotingExecution{
    protected static void createCouncilors() {
        m1 = new Councilors(localhost, portSet[0], responseProfile[0], true);
        m2 = new Councilors(localhost, portSet[1], responseProfile[1], true);
        m2.setNeedBreak(true);
        m3 = new Councilors(localhost, portSet[2], responseProfile[2], true);
        m4 = new Councilors(localhost, portSet[3], responseProfile[3], false);
        m5 = new Councilors(localhost, portSet[4], responseProfile[3], false);
        m6 = new Councilors(localhost, portSet[5], responseProfile[3], false);
        m7 = new Councilors(localhost, portSet[6], responseProfile[3], false);
        m8 = new Councilors(localhost, portSet[7], responseProfile[3], false);
        m9 = new Councilors(localhost, portSet[8], responseProfile[3], false);
    }

    public static void main(String[] args) {
        ProposeNOffline proposeNOffline = new ProposeNOffline();

        try {
            createCouncilors();
            startServer();
            Thread.sleep(500);
//            proposeNOffline.voting(m1);
//            proposeNOffline.voting(m2);
//            proposeNOffline.voting(m3);
            new Thread(new Voting(proposerN, endInfo, m1, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m2, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m3, portSet)).start();
            while (VotingExecution.endInfo.get() != 0) {
                Thread.sleep(100);
            }
            Thread.sleep(5000);
            proposeNOffline.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
