import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ContentServer {
    public static void main(String[] args) throws InterruptedException {
        // Get input parameter
        if (args.length == 2) {
            connect(args[0], args[1], "0", 5000);
        } else if (args.length == 3) {
            if (Integer.parseInt(args[2]) < 1000) {
                connect(args[0], args[1], args[2], 5000);
            } else {
                connect(args[0], args[1], "0", Integer.parseInt(args[2]));
            }
        } else {
            System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"+
                                "#           Please input correct inputs.            #\n" +
                                "#          The input should be one or two.          #\n" +
                                "#     The first one should be server and port.      #\n" +
                                "#        The second one should be file path.        #\n" +
                                "#        The third one(optional) should be          #\n" +
                                "#       survival time(smaller than 1000) or         #\n" +
                                "#         Heart beat time(lager than 1000).         #\n" +
                                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
    protected static int time = 0;
    protected static Socket socket;
    protected static void connect(String url, String path, String survival, int heartBeat) throws InterruptedException {
        try {
            String[] urls = url.split("\\:\\/\\/|\\:");
            // Create socket
            socket = new Socket(urls[1], Integer.parseInt(urls[2]));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ContentServer.time = 0;
            int num = (int) (Math.random()*100);
            System.out.println("Content server " + num + " connect");
            new Thread(new ContentServer_kill(survival)).start();
            Thread thread = new Thread(new ContentServer_send(socket, objectOutputStream, path, num, url, heartBeat));
            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            new Thread(new ContentServer_heart(socket, objectOutputStream, num)).start();
        } catch (ConnectException connectException) {
            ContentServer.time++;
            if (ContentServer.time <= 5) {
                System.out.println("No target server, please check the port. If it is alright, please wait." +
                        " it will connect again in 3 seconds.");
                Thread.sleep(3000);
                System.out.println("Try to connect: " + ContentServer.time);
                connect(url, path, survival, heartBeat);
            } else {
                System.out.println("No target server. Please check the port and try later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static int logicClock = 0;

    protected static void updateLogicClock() {
        logicClock++;
    }

    protected static void updateLogicClock(int time) {
        if (logicClock < time) {
            logicClock = time;
        }
        updateLogicClock();
    }
}

class ContentServer_kill implements Runnable {
    private String survival;

    ContentServer_kill(String survival) {
        this.survival = survival;
    }

    @Override
    public void run() {
        try {
            if (!Objects.equals(this.survival, "0")) {
                Thread.sleep(Integer.parseInt(survival) * 1000L);
                ContentServer.socket.close();
                System.out.println("kill");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ContentServer_send implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private String path, url;
    private int num, key, heartBeat;
    private boolean make_sense = true;

    ContentServer_send(
            Socket socket,
            ObjectOutputStream objectOutputStream,
            String path,
            int num,
            String url,
            int heartBeat) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream ;
        this.path = path;
        this.num = num;
        this.key = -1;
        this.url = url;
        this.heartBeat = heartBeat;
    }

    ContentServer_send(
            Socket socket,
            ObjectOutputStream objectOutputStream,
            String path,
            int num,
            String url,
            int heartBeat,
            int key) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream ;
        this.path = path;
        this.num = num;
        this.key = key;
        this.url = url;
        this.heartBeat = heartBeat;
    }

    private void try_connect() {
        try {
            ContentServer.time++;
            System.out.println("Disconnect, please wait. It will try again in 3 seconds.");
            Thread.sleep(3000);
            System.out.println("Try to connect: " + ContentServer.time);
            String[] urls = url.split("\\:\\/\\/|\\:");
            this.socket = new Socket(urls[1], Integer.parseInt(urls[2]));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            run();
        } catch (ConnectException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_key() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            MyFile[] myFile =(MyFile[]) objectInputStream.readObject();
            if (Objects.equals(myFile[0].getName(), "CS")) {
                if (myFile[0].getNum() == this.num) {
                    this.key = myFile[0].getKey();
                    ContentServer.updateLogicClock(myFile[0].getTime());
                    System.out.println("[" + ContentServer.logicClock + "]  " +
                            myFile[0].getName() + myFile[0].getNum() +
                            ": Status -> " + myFile[0].getStatus() + " - HTTP_CREATED" +
                            "\nGet permit and AggregationServer key: " + this.key +
                            "\n" + myFile[0].getName() + myFile[0].getNum() +
                            " --> " + myFile[0].getName() + myFile[0].getKey());
                    this.num = this.key;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_logic() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            MyFile[] myFile =(MyFile[]) objectInputStream.readObject();
            if (Objects.equals(myFile[0].getName(), "CS")) {
                if (myFile[0].getNum() == this.num) {
                    ContentServer.updateLogicClock(myFile[0].getTime());
                    if (Objects.equals(myFile[0].getStatus(), "200")) {
                        System.out.println("[" + ContentServer.logicClock + "]  " +
                                myFile[0].getName() + myFile[0].getNum() +
                                ": Status -> " + myFile[0].getStatus() +
                                "\nGet response.");
                    } else if (Objects.equals(myFile[0].getStatus(), "500")) {
                        this.make_sense = false;
                        System.out.println("[" + ContentServer.logicClock + "]  " +
                                myFile[0].getName() + myFile[0].getNum() +
                                ": Status -> " + myFile[0].getStatus() + " - Internal server error" +
                                "\nPlease check feed.");
                    } else if (Objects.equals(myFile[0].getStatus(), "400")) {
                        this.make_sense = false;
                        System.out.println("[" + ContentServer.logicClock + "]  " +
                                myFile[0].getName() + myFile[0].getNum() +
                                ": Status -> " + myFile[0].getStatus() +
                                "\nOther request.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Send request
//            System.out.println(num);
            if (this.key == -1) {
                ContentServer.updateLogicClock();
                MyFile myFile = new MyFile(num, "CS", ContentServer.logicClock);
                myFile.setStatus("PUT");
                MyFile[] re = {myFile};
                System.out.println("[" + ContentServer.logicClock + "]  " +
                        myFile.getName() + myFile.getNum() + ": Put request!");
                objectOutputStream.writeObject(re);
                objectOutputStream.flush();
                get_key();
            }
//            while (true) {
                ContentServer.time = 0;
                ContentServer.updateLogicClock();
                MyFile myFile = new MyFile(num, "CS", ContentServer.logicClock, this.key);
                myFile.setPath(path);
//                myFile.setUpdated(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(new Date()));
                MyFile[] re = myFile.read_file(myFile, path);
                System.out.println("[" + ContentServer.logicClock + "]  " +
                        myFile.getName() + myFile.getNum() + ": Read file!");
                ContentServer.updateLogicClock();
                objectOutputStream.writeObject(re);
                objectOutputStream.flush();
                System.out.println("[" + ContentServer.logicClock + "]  " +
                        myFile.getName() + myFile.getNum() + ": Send feed!");
                get_logic();
                if (make_sense) {
                    // Heart beat
//                Thread.sleep(this.heartBeat);
                    new ContentServer_heart(
                            socket, objectOutputStream, num, heartBeat, this.key, this.url, this.path).run();
                }
//            }
        } catch (SocketException socketException) {
            try {
                while (ContentServer.time < 5) {
                    try_connect();
                }
                if (ContentServer.time == 5) {
                    System.out.println("No target server. Please check the port and try later.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ContentServer_heart implements Runnable {
    private Socket socket;
    private  ObjectOutputStream objectOutputStream;
    private int num, heartBeat, key;
    private String url, path;
    private boolean lost;

    ContentServer_heart(
            Socket socket,
            ObjectOutputStream objectOutputStream,
            int num,
            int heartBeat,
            int key,
            String url,
            String path) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream;
        this.num = num;
        this.heartBeat = heartBeat;
        this.key = key;
        this.url = url;
        this.lost = false;
        this.path = path;
    }

    private void get_logic() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            MyFile[] myFile =(MyFile[]) objectInputStream.readObject();
            if (Objects.equals(myFile[0].getName(), "CS")) {
                if (myFile[0].getNum() == this.num) {
                    ContentServer.updateLogicClock(myFile[0].getTime());
                    System.out.println("[" + ContentServer.logicClock + "]  " +
                            myFile[0].getName() + myFile[0].getNum() +
                            ": Status -> " +myFile[0].getStatus() +
                            "\nGet response.");
                    this.lost = myFile[0].isLost();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void try_connect() {
        try {
            ContentServer.time++;
            System.out.println("Disconnect, please wait. It will try again in 3 seconds.");
            Thread.sleep(3000);
            System.out.println("Try to connect: " + ContentServer.time);
            String[] urls = url.split("\\:\\/\\/|\\:");
            this.socket = new Socket(urls[1], Integer.parseInt(urls[2]));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            run();
        } catch (ConnectException ignored) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(heartBeat);
                ContentServer.updateLogicClock();
                MyFile myFile = new MyFile(num, "CS", ContentServer.logicClock, this.key);
                myFile.setHeart(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(new Date()));
                MyFile[] re = {myFile};
                objectOutputStream.writeObject(re);
                objectOutputStream.flush();
                System.out.println("[" + ContentServer.logicClock + "]  " +
                        myFile.getName() + myFile.getNum() + ": Send heart beat! Connecting...");
                get_logic();
                if (this.lost) {
                    break;
                }
            }
            new ContentServer_send(socket, objectOutputStream, path, num, url, heartBeat, key).run();
        } catch (SocketException socketException) {
            try {
                while (ContentServer.time < 5) {
                    try_connect();
                }
                if (ContentServer.time == 5) {
                    System.out.println("No target server. Please check the port and try later.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}