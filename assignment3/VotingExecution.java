import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class VotingExecution {
    protected static final String localhost = "127.0.0.1";
    protected static final String[] responseProfile = {"P1", "P2", "P3", "P4", "P5", "P6", "P7"};
    protected static final int[] portSet = {8080, 8081, 8082, 8083, 8084, 8085, 8086, 8087, 8088};

    protected static Councilors m1, m2, m3, m4, m5, m6, m7, m8, m9;

    public static AtomicInteger proposerN = new AtomicInteger(0);
    public static AtomicInteger endInfo = new AtomicInteger(0);

    // Create different thread for every member
    protected static void startServer() {
        new Thread( () -> {
            try {
                m1.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m2.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m3.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m4.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m5.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m6.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m7.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m8.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> {
            try {
                m9.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void end() throws IOException {
        m1.close();
        m2.close();
        m3.close();
        m4.close();
        m5.close();
        m6.close();
        m7.close();
        m8.close();
        m9.close();
    }
}

class Voting implements Runnable {
    protected AtomicInteger proposerN, endInfo;
    protected Councilors councilors;
    protected int[] portSet;

    public Voting(
            AtomicInteger proposerN,
            AtomicInteger endInfo,
            Councilors councilors,
            int[] portSet) {
        this.proposerN = proposerN;
        this.endInfo = endInfo;
        this.councilors = councilors;
        this.portSet = portSet;
    }

    protected void prepareStage(Councilors councilors, int resN) {
        // Send prepare proposal to every member
        for (int i = 0; i < this.portSet.length; i++) {
            if (councilors.getPort() != portSet[i]) {
                int finalI = i;
                new Thread() {
                    @Override
                    public synchronized void run() {
                        try {
                            councilors.prepareStage(portSet[finalI], resN);
                        } catch (Exception e) {
                            if (Objects.equals(councilors.getResponseProfile(), "P7")) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.out.println(councilors.getCandidate() + "::"
                                        + portSet[finalI] + ":: No response");
                            } else {
                                System.out.println(councilors.getCandidate() + " -> "
                                        + portSet[finalI] + ": Loss connection");
                            }
                        }
                    }
                }.start();
            }
        }
    }

    protected void commitStage(Councilors councilors, int resN) {
        // Send commit proposal to every member
        for (int i = 0; i < portSet.length; i++) {
            if (councilors.getPort() != portSet[i]) {
                int finalI = i;
                new Thread() {
                    @Override
                    public synchronized void run() {
                        try {
                            councilors.commitStage(portSet[finalI]);
                        } catch (Exception e){
                            System.out.println("Loss connection");
                        }
                    }
                }.start();
            }
        }
    }

    protected void learnStage(Councilors councilors, int resN) {
        // Send voting result to every member
        for (int i = 0; i < portSet.length; i++) {
            if (councilors.getPort() != portSet[i]) {
                int finalI = i;
                new Thread() {
                    @Override
                    public synchronized void run() {
                        try {
                            councilors.learnStage(portSet[finalI]);
                        } catch (Exception e) {
                            System.out.println("Loss connection");
                        }
                    }
                }.start();
            }
        }
    }
    @Override
    public void run() {
        endInfo.incrementAndGet();
//            System.out.println(endInfo.get() + "-------------------------------------------------");
        while (true) {
            try {
                // Prepare stage
                int resN = proposerN.incrementAndGet();
                councilors.setConfirm(1);
                prepareStage(councilors, resN);
                Thread.sleep((long) (Math.random()*5000));
                if (councilors.isNeedBreak()){
                    break;
                }
//                    System.out.println(councilors.getCandidate() + " >>>>>> " + councilors.getConfirm());
                if (Objects.equals(councilors.getResponseProfile(), "P7")) {
                    break;
                }
                // Get majority can go to commit stage
                if (councilors.getConfirm() > portSet.length/2) {
                    councilors.setConfirm(1);
                    System.out.println(councilors.getCandidate() +
                            ": get majority support, begin commit stage");
                    commitStage(councilors, resN);
                    Thread.sleep((long) (Math.random()*5000));
//                        System.out.println(councilors.getCandidate() + " >>>>>> " + councilors.getConfirm());

                    // Get majority can go to learn stage
                    if (councilors.getConfirm() > portSet.length/2) {
                        System.out.println(councilors.getCandidate() +
                                ": get majority support, begin learn stage");
                        learnStage(councilors, resN);
                        Thread.sleep((long) (Math.random()*5000));
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        endInfo.decrementAndGet();
//            System.out.println(endInfo.get() + "-------------------------------------------------");
    }
}
