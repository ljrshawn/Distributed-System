public class ImmediateResponse extends VotingExecution{
    protected static void createCouncilors() {
        m1 = new Councilors(localhost, portSet[0], responseProfile[0], true);
        m2 = new Councilors(localhost, portSet[1], responseProfile[0], true);
        m3 = new Councilors(localhost, portSet[2], responseProfile[0], true);
        m4 = new Councilors(localhost, portSet[3], responseProfile[0], false);
        m5 = new Councilors(localhost, portSet[4], responseProfile[0], false);
        m6 = new Councilors(localhost, portSet[5], responseProfile[0], false);
        m7 = new Councilors(localhost, portSet[6], responseProfile[0], false);
        m8 = new Councilors(localhost, portSet[7], responseProfile[0], false);
        m9 = new Councilors(localhost, portSet[8], responseProfile[0], false);
    }

    public static void main(String[] args) {
        ImmediateResponse immediateResponse = new ImmediateResponse();

        try {
            createCouncilors();
            startServer();
            Thread.sleep(500);
            new Thread(new Voting(proposerN, endInfo, m1, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m2, portSet)).start();
            new Thread(new Voting(proposerN, endInfo, m3, portSet)).start();
            while (VotingExecution.endInfo.get() != 0) {
                Thread.sleep(100);
            }
            immediateResponse.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
