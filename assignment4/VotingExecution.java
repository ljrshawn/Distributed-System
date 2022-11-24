import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class VotingExecution {
    protected static final String localhost = "127.0.0.1";
    protected static final String[] responseProfile = {"P1", "P2", "P3", "P4", "P5", "P6", "P7"};
    protected static final int[] portSet = {8080, 8081, 8082, 8083, 8084,
                                            8085};//,
//                                            8095, 8096, 8097, 8098, 8099};//,
//                                            8100, 8101, 8102, 8103, 8104};//,
//                                            8105, 8106, 8107, 8108, 8109,
//                                            8110, 8111, 8112, 8113, 8114};//,
                                           // 8115, 8116, 8117, 8118, 8119};

    protected static Councilors m1, m2, m3, m4, m5, m6;
                                //m16, m17, m18, m19, m20;//, m21, m22, m23, m24, m25;//, m26, m27, m28, m29, m30, m31, m32, m33, m34, m35;//, m36, m37, m38, m39, m40;

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

//        new Thread( () -> {
//            try {
//                m7.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m8.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m9.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m10.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m11.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m12.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m13.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m14.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m15.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m16.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m17.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m18.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m19.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m20.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m21.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m22.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m23.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m24.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m25.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m26.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m27.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m28.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m29.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m30.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m31.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m32.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m33.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m34.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m35.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

//        new Thread( () -> {
//            try {
//                m36.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m37.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m38.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m39.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread( () -> {
//            try {
//                m40.startServer();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    protected void end() throws IOException {
        m1.close();
        m2.close();
        m3.close();
        m4.close();
        m5.close();
        m6.close();
//        m7.close();
//        m8.close();
//        m9.close();
//        m10.close();
//        m11.close();
//        m12.close();
//        m13.close();
//        m14.close();
//        m15.close();
//        m16.close();
//        m17.close();
//        m18.close();
//        m19.close();
//        m20.close();
//        m21.close();
//        m22.close();
//        m23.close();
//        m24.close();
//        m25.close();
//        m26.close();
//        m27.close();
//        m28.close();
//        m29.close();
//        m30.close();
//        m31.close();
//        m32.close();
//        m33.close();
//        m34.close();
//        m35.close();
//        m36.close();
//        m37.close();
//        m38.close();
//        m39.close();
//        m40.close();
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
