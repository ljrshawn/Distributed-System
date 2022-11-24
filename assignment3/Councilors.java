import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Councilors {
    private ServerSocket accServer;
    private Socket accSocket;
    private String localhost;

    private int port;
    private String responseProfile;
    private String candidate;
    private String votingName = "";
    private int resN = -1;
    private int confirm = 1;
    private Proposal proposal = new Proposal();
    private Map<Integer, String> portAcceptor = new HashMap<>();
    private static final int delayTime = 50;
    private static boolean connectDelay = false;
    private boolean needBreak = false;
    private final boolean needProposal;

    Councilors(String localhost, int port, String responseProfile, boolean needProposal) {
        this.localhost = localhost;
        this.port = port;
        this.responseProfile = responseProfile;
        this.candidate = "M" + (port - 8079);
        this.needProposal = needProposal;
    }

    public static boolean isConnectDelay() {
        return connectDelay;
    }

    public static void setConnectDelay(boolean connectDelay) {
        Councilors.connectDelay = connectDelay;
    }

    /**  Start Server and receive proposal  **/
    // Create server to receive proposal or commit
    public void startServer() throws IOException {
        // Clear history leader information
        File f = new File(Objects.requireNonNull(Councilors.class.getResource("")).getPath());
        File[] result = f.listFiles();
        String regex = this.candidate + "_leader.*.txt";
        Pattern pattern = Pattern.compile(regex);
        assert result != null;
        for(File fs : result){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                Files.delete(Paths.get(matcher.group(0).toString()));
            }
        }

        try {

            // start server
            this.accServer = new ServerSocket(this.port);
            System.out.println(this.candidate + " is online!");

            while (true) {
                this.accSocket = this.accServer.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(this.accSocket.getInputStream());

                Proposal message = (Proposal) objectInputStream.readObject();
                // Never response
                if (Objects.equals(this.responseProfile, "P7")) {
                    this.accSocket.close();
                    this.accServer.close();
                    break;
                }
                request(this.accSocket, message);
                // For offline
                if (this.needBreak) {
                    this.accSocket.close();
                    this.accServer.close();
                    break;
                }
            }
        } catch (SocketException e) {
            if (Objects.equals(this.responseProfile, "P7")) {
                System.out.println(this.candidate + ": Never response");
            } else {
                System.out.println(this.candidate + ": Leader has been elected: " + this.votingName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void request(Socket socket, Proposal message)
            throws IOException, InterruptedException {
        System.out.println("Acceptor: " + this.candidate + ": receive proposal from No#" + message.getSerialNumber());
        if (Objects.equals(message.getStatus(), "prepare")) {
            prepareReq(socket, message);
        } else if (Objects.equals(message.getStatus(), "commit")) {
            commitReq(socket, message);
            // For learn
        } else {
            learnReq(socket, message);
        }
    }

    public void prepareReq(Socket socket, Proposal message) throws IOException, InterruptedException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        this.proposal = new Proposal(this.resN, null);
        int pResN = message.getSerialNumber();

        // The proposal member will not vote to other, because they can vote themselves
        if (this.needProposal) {
            this.proposal.setStatus("reject");
        } else {
            // The member have not received commit
            if (Objects.equals(this.votingName, "")) {
//            System.out.println(this.candidate + "->" + this.voteRight);

                if (pResN > this.resN) {
                    this.resN = pResN;
                    this.proposal.setSerialNumber(this.resN);
                    this.proposal.setStatus("promise");

                } else {
                    this.proposal.setStatus("reject");
                }
                // The member have received commit, it will transform voting information to proposal
            } else {
                if (pResN > this.resN) {
                    this.resN = pResN;
                    this.proposal.setSerialNumber(this.resN);
                    this.proposal.setValue(this.votingName);
                    this.proposal.setStatus("votingPromised");

                } else {
                    this.proposal.setStatus("reject");
                }
            }
        }

        System.out.println("Acceptor:: " + this.candidate +
                " :: Prepare action for No#" + pResN + ": " + this.proposal.getStatus());

        // Random sleep to simulate different response time or profiles
        double ran = Math.random();
        if (Objects.equals(this.responseProfile, "P2")) {
            if (ran > 0.3) {
                Thread.sleep(delayTime * 100);
            }
        }
        if (Objects.equals(this.responseProfile, "P3")) {
            if (ran < 0.3) {
                Thread.sleep(delayTime * 100);
            } else {
                Thread.sleep(delayTime);
            }
        }
        if (Objects.equals(this.responseProfile, "P4")) {
            Thread.sleep((int)(Math.random()+4) * delayTime);
        }
        if (Objects.equals(this.responseProfile, "P5")) {
            Thread.sleep(2 * delayTime);
        }
        if (Objects.equals(this.responseProfile, "P6")) {
            Thread.sleep(5 * delayTime);
        }

        objectOutputStream.writeObject(this.proposal);
        objectOutputStream.flush();

    }

    public void commitReq(Socket socket, Proposal message) throws IOException, InterruptedException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        this.proposal = new Proposal(this.resN, null);
        int pResN = message.getSerialNumber();
        // The proposal member will reject other commit
        if (this.needProposal) {
            this.proposal.setStatus("reject");
        } else {
            // Only larger number ID can get response
            if (pResN >= this.resN) {
                this.resN = pResN;
                this.votingName = message.getValue();
                this.proposal.setSerialNumber(this.resN);
                this.proposal.setValue(this.votingName);
                this.proposal.setStatus("promise");

            } else {
                this.proposal.setStatus("reject");
            }
        }
        System.out.println("Acceptor:: " + this.candidate +
                " :: Commit action for No#" + pResN + ": " + this.proposal.getStatus());

        // Random sleep to simulate different response time or profiles
        double ran = Math.random();
        if (Objects.equals(this.responseProfile, "P2")) {
            if (ran > 0.3) {
                Thread.sleep(delayTime * 100);
            }
        }
        if (Objects.equals(this.responseProfile, "P3")) {
            if (ran < 0.3) {
                Thread.sleep(delayTime * 100);
            } else {
                Thread.sleep(delayTime);
            }
        }
        if (Objects.equals(this.responseProfile, "P4")) {
            Thread.sleep((int)(Math.random()+4) * delayTime);
        }
        if (Objects.equals(this.responseProfile, "P5")) {
            Thread.sleep(2 * delayTime);
        }
        if (Objects.equals(this.responseProfile, "P6")) {
            Thread.sleep(5 * delayTime);
        }

        objectOutputStream.writeObject(this.proposal);
        objectOutputStream.flush();

    }

    public void learnReq(Socket socket, Proposal message) throws IOException {
        // Every member should learn the voting result
        String path = this.candidate + "_leader_name" + ".txt";
        // Backup voting result
        File backup = new File(path);
        if (!backup.exists()) {
            backup.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(backup);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("Election result: " + message.getValue());
        bufferedWriter.close();
        fileWriter.close();
    }

    /**
     * Proposal process:
     *                  prepare
     *                  commit
     *                  learn
     *  **/
    // First Stage
    public void prepareStage(int aimPort, int proposerN) throws InterruptedException, IOException {
        String aimCandidate = "M" + (aimPort - 8079);
        System.out.println(this.candidate + ":: prepare :: connect to " + aimCandidate);

        Socket connectSocket = null;
        int tryTime = 0;
        while (tryTime < 3) {
            try {
                // Connect the member who have voting right
                connectSocket = new Socket(this.localhost, aimPort);
                tryTime = 0;
            } catch (ConnectException ce) {
                tryTime++;
                System.out.println(this.candidate + ":: prepare :: failure to connect " +
                        aimCandidate + ". Try again in 3 seconds");
                Thread.sleep(3000);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        if (connectSocket == null) {
            System.out.println(this.candidate + ":: prepare :: failure to connect " + aimCandidate);
            return;
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(connectSocket.getOutputStream());

        // Prepare proposal information
        this.resN = proposerN;
        this.proposal = new Proposal(this.resN, null);
        this.proposal.setStatus("prepare");

//        System.out.println(this.candidate + "->" + this.resN);
        objectOutputStream.writeObject(this.proposal);
        objectOutputStream.flush();

        // Receive voting response
        Proposal recReq = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(connectSocket.getInputStream());
        try {
            recReq = (Proposal) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(this.candidate + ":: prepare :: failure to connect");
        }
        if (recReq != null) {
            if (Objects.equals(recReq.getStatus(), "promise")) {
                this.confirm++;
            } else if (Objects.equals(recReq.getStatus(), "votingPromised")) {
                this.confirm++;
                this.votingName = recReq.getValue();
                System.out.println(this.candidate + ":: prepare :: change vote to " + this.votingName);
            } else {
                System.out.println(this.candidate + ":: prepare :: " + aimCandidate + " promise reject");
            }
        }
    }

    // Second Stage
    public void commitStage(int aimPort) throws InterruptedException, IOException {
        String aimCandidate = "M" + (aimPort - 8079);
        System.out.println(this.candidate + ":: commit :: connect to " + aimCandidate);

        Socket connectSocket = null;
        int tryTime = 0;
        while (tryTime < 3) {
            try {
                // Connect the member to get commission response
                connectSocket = new Socket(this.localhost, aimPort);
                tryTime = 0;
            } catch (ConnectException ce) {
                tryTime++;
                System.out.println(this.candidate + ":: commit :: failure to connect " +
                        aimCandidate + ". Try again in 3 seconds");
                Thread.sleep(3000);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        if (connectSocket == null) {
            System.out.println(this.candidate + ":: commit :: failure to connect " + aimCandidate);
            return;
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(connectSocket.getOutputStream());
        if (Objects.equals(this.votingName, "")) {
            this.votingName = this.candidate;
        }
        this.proposal = new Proposal(this.resN, this.votingName);
        this.proposal.setStatus("commit");

//        System.out.println(this.candidate + "->" + this.resN);
        objectOutputStream.writeObject(this.proposal);
        objectOutputStream.flush();

        // Receive voting response
        Proposal recReq = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(connectSocket.getInputStream());
        try {
            recReq = (Proposal) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(this.candidate + ":: commit :: failure to connect");
        }
        if (recReq != null) {
            if (Objects.equals(recReq.getStatus(), "promise")){
                this.confirm++;
            } else {
                System.out.println(this.candidate + ":: commit ::  " + aimCandidate + " commit reject");
            }
        }
    }

    // Learn stage
    public void learnStage(int aimPort) throws InterruptedException, IOException {
        String aimCandidate = "M" + (aimPort - 8079);
        System.out.println(this.candidate + ":: learn :: connect to " + aimCandidate);

        Socket connectSocket = null;
        int tryTime = 0;
        while (tryTime < 3) {
            try {
                // Connect all the members to let them learn result of voting
                connectSocket = new Socket(this.localhost, aimPort);
                tryTime = 0;
            } catch (ConnectException ce) {
                tryTime++;
                System.out.println(this.candidate + ":: learn :: failure to connect " +
                        aimCandidate + ". Try again in 3 seconds");
                Thread.sleep(3000);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        if (connectSocket == null) {
            System.out.println(this.candidate + ":: learn :: failure to connect " + aimCandidate);
            return;
        }
        // Prepare result information
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(connectSocket.getOutputStream());
        if (Objects.equals(this.votingName, "")) {
            this.votingName = this.candidate;
        }
        this.proposal = new Proposal(this.resN, this.votingName);
        this.proposal.setStatus("learn");
        objectOutputStream.writeObject(this.proposal);
        objectOutputStream.flush();
    }

    /** Close receive server **/
    public void close() throws IOException {
//        this.accSocket.close();
        this.accServer.close();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public Map<Integer, String> getPortAcceptor() {
        return portAcceptor;
    }

    public void setPortAcceptor(Map<Integer, String> portAcceptor) {
        this.portAcceptor = portAcceptor;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public boolean isNeedBreak() {
        return this.needBreak;
    }

    public void setNeedBreak(boolean needBreak) {
        this.needBreak = needBreak;
    }

    public String getResponseProfile() {
        return responseProfile;
    }

    public void setResponseProfile(String responseProfile) {
        this.responseProfile = responseProfile;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public String getVotingName() {
        return votingName;
    }

    public void setVotingName(String votingName) {
        this.votingName = votingName;
    }
}