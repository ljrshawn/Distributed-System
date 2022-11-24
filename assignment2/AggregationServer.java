import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AggregationServer {
    public static void main(String[] args) throws InterruptedException, IOException {
//         Check the input
        if (args.length == 0) {
            connect("4567", "0");
        } else if (args.length == 1) {
            connect(args[0], "0");
        } else if (args.length == 2) {
            connect(args[0], args[1]);
        } else {
            System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"+
                                "#           Please input correct inputs.             \n#" +
                                "#       The input should be null or one or two.      \n#" +
                                "#       The first one(optional) should be port.      \n#" +
                                "#  The second one(optional) should be survival time. \n#" +
                                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    protected static void connect(String port, String survival) throws InterruptedException, IOException {
        // Create server socket
        AggregationServer.updateLogicClock();
        System.out.println("[" + AggregationServer.logicClock + "]  " + "Server ready...");
        // Create server socket
        serverSocket = new ServerSocket(Integer.parseInt(port));
        kill = true;
        BlockingQueue<Socket> queue = new ArrayBlockingQueue<>(30);
        try {
            // Create thread to check expired feed
            new Thread(new AggregationServer_Check_Remove()).start();
            // Create thread to kill socket after a period of time
            new Thread(new AggregationServer_kill(survival)).start();
            while (true) {
                // Accept request
                socket = serverSocket.accept();
                AggregationServer.times = 0;
                // Save different socket request
                // Prevent losing when different socket request at the same time
                queue.add(socket);
                Thread.sleep(10);
                // Create different thread to process socket request
                new Thread(new AggregationServer_listen(queue.remove())).start();
            }
        } catch (SocketException e) {
            // When socket broken, try to reconnect
            AggregationServer.times ++;
            if (AggregationServer.times <= 3) {
                System.out.println("Broken! Please wait. It will try again in 3 seconds.");
                Thread.sleep(3000);
                AggregationServer.updateLogicClock();
                System.out.println("[" + AggregationServer.logicClock + "]  " + AggregationServer.times +
                        ": Try to rebuild socket!");
                connect(port, "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static ServerSocket serverSocket;
    protected static Socket socket;
    protected static int logicClock = 0;
    protected volatile static boolean lock = true;
    protected static int times = 0;
    protected volatile static boolean kill = true;
    protected static int key = 101;

    protected static void updateLogicClock() {
        logicClock++;
//        System.out.println(logicClock);
    }

    protected static void updateLogicClock(int time) {
        if (logicClock < time) {
            logicClock = time;
        }
        updateLogicClock();
    }

    // Obtain the required documents
    protected static void getFile() {
        File f = new File(Objects.requireNonNull(AggregationServer.class.getResource("")).getPath());
        File[] result = f.listFiles();
        assert result != null;
        List<File> fileList = Arrays.asList(result);
        Collections.sort(fileList);
        String regex="CS.*.txt";
        Pattern pattern = Pattern.compile(regex);
        for(File fs : fileList){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                System.out.println(matcher.group(0).toString());
            }
        }
    }

//    protected static void writeLatestTime(MyFile myFile) {
//        try {
//            PrintStream console = System.out;
//            String path = "update" + myFile.getNum() + ".txt";
//            PrintStream printStream = new PrintStream(path);
//            System.setOut(printStream);
//            System.out.println(myFile.getUpdated());
//            System.setOut(console);
//            AggregationServer.updateLogicClock();
//            System.out.println("[" + AggregationServer.logicClock + "]  " + "Save update time for " +
//                    myFile.getName() + myFile.getNum());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // Delete the required documents
    protected static void removeFile(MyFile myFile) throws IOException {
        String name = myFile.getName() + myFile.getKey() + ".txt|HeartBeat" + myFile.getKey() + ".txt";
        ArrayList<String> file = new ArrayList<>();
        File f = new File(Objects.requireNonNull(RemoveFile.class.getResource("")).getPath());
        File[] result = f.listFiles();
        Pattern pattern = Pattern.compile(name);
        for(File fs : result){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                file.add(matcher.group(0).toString());
            }
        }
        if (!file.isEmpty()) {
            for (String fs : file) {
                Files.delete(Paths.get(fs));
            }
            AggregationServer.updateLogicClock();
            System.out.println("[" + AggregationServer.logicClock + "]  " +
                    myFile.getName() + myFile.getNum() + ": Disconnect! Cleaning feed successfully!");
        }
    }
}

class AggregationServer_kill implements Runnable {
    private String survival;

    AggregationServer_kill(String survival) {
        this.survival = survival;
    }

    @Override
    public void run() {
        try {
            if (!Objects.equals(survival, "0")) {
                Thread.sleep(Integer.parseInt(survival) * 1000L);
                AggregationServer.kill = false;
                if (AggregationServer.socket != null) {
                    AggregationServer.socket.close();
                }
                AggregationServer.serverSocket.close();
                System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                                    "~~~    Kill AggregationServer!  ~~~\n" +
                                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AggregationServer_Check_Remove implements Runnable {

    @Override
    public void run() {
        while (true) {
            if (!AggregationServer.kill) {
                break;
            }
            try {
                // Find that saves the latest time file
                File f = new File(Objects.requireNonNull(AggregationServer.class.getResource("")).getPath());
                File[] result = f.listFiles();
                ArrayList<String> file = new ArrayList<>();
                String regex="HeartBeat.*.txt";
                Pattern pattern = Pattern.compile(regex);
                for(File fs : result){
                    Matcher matcher = pattern.matcher(fs.getName());
                    while(matcher.find()){
                        file.add(matcher.group(0).toString());
                    }
                }
                // Get current time
                String current = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(new Date());
                String[] c_time = current.split("T|-|\\:|Z");
                // Check the content server is expired or not
                for (String fs : file) {
                    Scanner input = new Scanner(new File(fs));
                    String[] time = String.join(":", input.next()).split("T|-|\\:|Z");
                    if (Integer.parseInt(c_time[0]) == Integer.parseInt(time[0]) &
                            Integer.parseInt(c_time[1]) == Integer.parseInt(time[1]) &
                            Integer.parseInt(c_time[2]) == Integer.parseInt(time[2]) &
                            Integer.parseInt(c_time[3]) == Integer.parseInt(time[3]) &
                            Integer.parseInt(c_time[4]) == Integer.parseInt(time[4]) &
                            Integer.parseInt(c_time[c_time.length -1]) - Integer.parseInt(time[time.length -1])
                                    <= 12) {} else if (Integer.parseInt(c_time[0]) == Integer.parseInt(time[0]) &
                            Integer.parseInt(c_time[1]) == Integer.parseInt(time[1]) &
                            Integer.parseInt(c_time[2]) == Integer.parseInt(time[2]) &
                            Integer.parseInt(c_time[3]) == Integer.parseInt(time[3]) &
                            Integer.parseInt(c_time[4]) - Integer.parseInt(time[4]) == 1 &
                            Integer.parseInt(c_time[c_time.length -1]) + 60 - Integer.parseInt(time[time.length -1])
                                    <= 12){} else {
                        // If the content server expired, delete the saved feed
                        Files.delete(Paths.get(fs));
                        Files.delete(Paths.get("CS" + fs.split("\\.")[0].substring(9) + ".txt"));
                        AggregationServer.updateLogicClock();
                        System.out.println("[" + AggregationServer.logicClock + "]  " +
                                "CS" + fs.split("\\.")[0].substring(9) +
                                ": Disconnect! Cleaning feed successfully!");
                    }
                }
                // Every second to check once
                Thread.sleep(1000);
            } catch (NoSuchFileException ignored) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

//class AggregationServer_check implements Runnable {
//    private MyFile myFile;
//
//    AggregationServer_check(MyFile myFile) {
//        this.myFile = myFile;
//    }
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(13000);
//            String path = "update" + myFile.getNum() + ".txt";
//            Scanner input = new Scanner(new File(path));
//            String[] time = input.nextLine().split("T|-|\\:|Z");
//            String current = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(new Date());
//            String[] c_time = current.split("T|-|\\:|Z");
//            if (Integer.parseInt(c_time[c_time.length -1]) - Integer.parseInt(time[time.length -1]) > 12) {
//                AggregationServer.removeFile(myFile);
//            }
//        }  catch (FileNotFoundException ignored) {} catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

class AggregationServer_listen implements Runnable {
    private Socket socket;
    private MyFile temFile;
    AggregationServer_listen(Socket socket) {
        this.socket = socket;
    }

    private void send_key(MyFile[] myFiles) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(myFiles);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check_key() {
        File f = new File(Objects.requireNonNull(AggregationServer.class.getResource("")).getPath());
        File[] result = f.listFiles();
        String regex="HeartBeat.*.txt";
        Pattern pattern = Pattern.compile(regex);
        int oldest = 1000;
        int feeds = 0;
        for(File fs : result){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                feeds++;
                int k = Integer.parseInt(matcher.group(0).toString().split("\\.")[0].substring(9));
                if (AggregationServer.key <= k) {
                    AggregationServer.key++;
                }
                if (oldest >= k) {
                    oldest = k;
                }
            }
        }
        // The aggregation sever can only save latest 20 feeds
        if (feeds == 20) {
            String name = "CS" + oldest + ".txt";
            pattern = Pattern.compile(name);
            for(File fs : result) {
                Matcher matcher = pattern.matcher(fs.getName());
                while (matcher.find()) {
                    try {
                        Files.delete(Paths.get(matcher.group(0).toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void send_logic(MyFile[] myFiles) {
        try {
            AggregationServer.updateLogicClock();
            System.out.println("[" + AggregationServer.logicClock + "]  " +
                    myFiles[0].getName() + myFiles[0].getNum() + ": Send receive confirm!");
            myFiles[0].setTime(AggregationServer.logicClock);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(myFiles);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write_heartbeat(MyFile[] myFile) {
        try {
            PrintStream console = System.out;
            String path = "HeartBeat" + myFile[0].getKey() + ".txt";
            PrintStream printStream = new PrintStream(path);
            System.setOut(printStream);
            System.out.println(myFile[0].getHeart());
            System.setOut(console);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean check_feed(MyFile myFile) {
        File f = new File(Objects.requireNonNull(AggregationServer.class.getResource("")).getPath());
        File[] result = f.listFiles();
        String regex= myFile.getName() + myFile.getKey() + ".txt";
        Pattern pattern = Pattern.compile(regex);
        boolean lost = true;
        for(File fs : result){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                lost = false;
            }
        }
        if (lost) {
            int feeds = 0;
            regex="CS.*.txt";
            pattern = Pattern.compile(regex);
            for(File fs : result){
                Matcher matcher = pattern.matcher(fs.getName());
                while(matcher.find()){
                    feeds++;
                }
            }
            if (feeds == 20) {
                lost = false;
            }
        }
        return lost;
    }

    @Override
    public void run() {
        try {
            // Get request
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());

            while (true) {
                MyFile[] myFile =(MyFile[]) objectInputStream.readObject();
//                if (!AggregationServer.kill) {
//                    this.socket.close();
//                    break;
//                }
                AggregationServer.times = 0;
                while (!AggregationServer.lock) {
                    Thread.sleep(10);
                }
                while (AggregationServer.lock) {
                    // Only one thread can work in this loop
                    AggregationServer.lock = false;
                    AggregationServer.updateLogicClock(myFile[0].getTime());
                    // If receive is heart beat
                    if (!Objects.equals(myFile[0].getHeart(), null)) {
                        System.out.println("[" + AggregationServer.logicClock + "]  " +
                                myFile[0].getName() + myFile[0].getNum() + ": Connecting!");
                        write_heartbeat(myFile);
                        myFile[0].setStatus("204");
                        if (check_feed(myFile[0])) {
                            myFile[0].setLost(true);
                        }
                        send_logic(new MyFile[]{myFile[0]});
                    } else {
                        // If receive is from content server
                        if (Objects.equals(myFile[0].getName(), "CS")) {
                            if (!Objects.equals(myFile[0].getStatus(), null)) {
                                if (Objects.equals(myFile[0].getStatus(), "PUT")) {
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            "Receive PUT request from " +
                                            myFile[0].getName() + myFile[0].getNum());
                                    check_key();
                                    AggregationServer.updateLogicClock();
                                    myFile[0].setKey(AggregationServer.key);
                                    myFile[0].setTime(AggregationServer.logicClock);
                                    myFile[0].setStatus("201");
                                    AggregationServer.key++;
                                    send_key(myFile);
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            myFile[0].getName() + myFile[0].getNum() + ": Sending status and key!");
                                } else {
                                    AggregationServer.updateLogicClock();
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            myFile[0].getName() + myFile[0].getNum() + ": Send request confirm!");
                                    myFile[0].setTime(AggregationServer.logicClock);
                                    myFile[0].setStatus("400");
                                    ObjectOutputStream objectOutputStream =
                                            new ObjectOutputStream(socket.getOutputStream());
                                    objectOutputStream.writeObject(myFile);
                                    objectOutputStream.flush();
                                }
                            } else {
                                // Save the feed from content server
                                System.out.println("[" + AggregationServer.logicClock + "]  " +
                                        myFile[0].getName() + myFile[0].getNum() + ": Receive feed!");
                                AggregationServer.updateLogicClock(myFile[0].getTime());
                                if(!MyFile.write_file(myFile)) {
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            "AggregationServer: The data from " + myFile[0].getName() +
                                            myFile[0].getNum() + " does not make sense.");
                                    myFile[0].setStatus("500");
                                    send_logic(new MyFile[]{myFile[0]});
                                } else {
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            "AggregationServer: The data from " + myFile[0].getName() +
                                            myFile[0].getNum() + " has been saved in " + myFile[0].getName() +
                                            myFile[0].getKey() + ".txt");
                                    myFile[0].setHeart(
                                            new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
                                                    .format(new Date()));
                                    // Save the receiving time
                                    write_heartbeat(myFile);
                                    myFile[0].setStatus("200");
                                    send_logic(new MyFile[]{myFile[0]});
//                            AggregationServer.writeLatestTime(myFile[0]);
//                            new Thread(new AggregationServer_check(myFile[0])).start();
                                }
                            }
                        } else {
                            // If receive is from client
                            if (!Objects.equals(myFile[0].getStatus(), null)) {
                                if (Objects.equals(myFile[0].getStatus(), "GET")) {
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            myFile[0].getName() + myFile[0].getNum() + " need feed!");
                                    PrintStream console = System.out;
                                    String path = "fileList" + myFile[0].getNum() + ".txt";
                                    PrintStream printStream = new PrintStream(path);
                                    System.setOut(printStream);
                                    // Get feed list
                                    AggregationServer.getFile();
                                    System.setOut(console);
                                    AggregationServer.updateLogicClock();
                                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                                            "Searching data for " +
                                            myFile[0].getName() + myFile[0].getNum());
                                    // Send feed to client
                                    new AggregationServer_send(socket, path, myFile[0].getNum()).run();
                                } else {
                                    try {
                                        AggregationServer.updateLogicClock();
                                        System.out.println("[" + AggregationServer.logicClock + "]  " +
                                                myFile[0].getName() + myFile[0].getNum() + ": Send request confirm!");
                                        myFile[0].setTime(AggregationServer.logicClock);
                                        myFile[0].setStatus("400");
                                        ObjectOutputStream objectOutputStream =
                                                new ObjectOutputStream(socket.getOutputStream());
                                        objectOutputStream.writeObject(myFile);
                                        objectOutputStream.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                this.temFile = myFile[0];
                AggregationServer.lock = true;
            }
        } catch (NullPointerException e) {
            System.out.println("[" + AggregationServer.logicClock + "]  " + "Nothing connect!");
        } catch (EOFException e) {
            try {
                if (Objects.equals(this.temFile.getName(), "CS")) {
                    AggregationServer.removeFile(this.temFile);
                } else {
                    System.out.println("[" + AggregationServer.logicClock + "]  " +
                            this.temFile.getName() + this.temFile.getNum() + ": Disconnect!");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SocketException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class  AggregationServer_send implements Runnable {
    private Socket socket;

    private String path;

    private int num;

    AggregationServer_send(Socket socket, String path, int num) {
        this.socket = socket;
        this.path = path;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            // Send response
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            AggregationServer.updateLogicClock();
            Scanner input  = new Scanner(new File(path));
            List<String> paths = new ArrayList<>();
            while (input.hasNext()){
                paths.add(input.next());
            }

            MyFile myFile = new MyFile(num, "Client", AggregationServer.logicClock);
            myFile.setStatus("201");
            MyFile[] re = new MyFile[0];
            for (String i :paths) {
                MyFile first = new MyFile(num, "Client", AggregationServer.logicClock);
                first.setStatus("201");
                MyFile[] tem = myFile.read_file(first, i);
                MyFile[] update = new MyFile[re.length + tem.length];
                System.arraycopy(re, 0, update, 0, re.length);
                System.arraycopy(tem, 0, update, re.length, tem.length);
                re = update;
            }
            if (re.length == 0) {
                re = new MyFile[]{myFile};
            }

            System.out.println("[" + AggregationServer.logicClock + "]  " + "Send feed to " +
                    myFile.getName() + myFile.getNum());
            objectOutputStream.writeObject(re);
            objectOutputStream.flush();
            Files.delete(Paths.get(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
