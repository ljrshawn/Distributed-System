import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;

public class GETClient {

    public static void main(String[] args) throws InterruptedException {
        connect(args[0]);
    }

    private static void connect(String url) throws InterruptedException {
        try {
            String[] urls = url.split("\\:\\/\\/|\\:");
            // Create socket
            Socket socket = new Socket(urls[1], Integer.parseInt(urls[2]));
            int num = (int) (Math.random()*100);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send Get request
            new Thread(new GETClient_send(socket, objectOutputStream, num)).start();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Receive feed
            new Thread(new GETClient_listen(socket, objectInputStream, num)).start();
        } catch (ConnectException connectException) {
            GETClient.time++;
            if (GETClient.time <= 3) {
                System.out.println("No target server, please check the port. If it is alright, please wait." +
                        " it will connect again in 3 seconds.");
                Thread.sleep(3000);
                System.out.println("Try to connect: " + GETClient.time);
                connect(url);
            } else {
                System.out.println("No target server. Please check the port and try later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int time = 0;
    protected static int logicClock = 0;

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
}

class GETClient_listen implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private int num;

    GETClient_listen(Socket socket, ObjectInputStream objectInputStream, int num) {
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.num = num;
    }
    @Override
    public void run() {
        try {
            // Get response from AS
            MyFile[] myFile =(MyFile[]) objectInputStream.readObject();
            if (Objects.equals(myFile[0].getStatus(), "400")) {
                GETClient.updateLogicClock(myFile[0].getTime());
                System.out.println("[" + GETClient.logicClock + "]  " +
                        myFile[0].getName() + myFile[0].getNum() +
                        ": Status -> " + myFile[0].getStatus() +
                        "\nNo meaning request.");
            } else {
                if (myFile.length > 1) {
                    GETClient.updateLogicClock(myFile[0].getTime());
                    if (myFile[0].getNum() == this.num) {
                        MyFile.write_file(myFile);
                        System.out.println("[" + GETClient.logicClock + "]  " +
                                myFile[0].getName() + myFile[0].getNum() +
                                ": Status -> " + myFile[0].getStatus() + " - HTTP_CREATED" +
                                "\nReceive data and save succeed!");
                    }
                } else {
                    GETClient.updateLogicClock();
                    System.out.println("[" + GETClient.logicClock + "]  " +
                            myFile[0].getName() + myFile[0].getNum() +
                            ": Status -> " + myFile[0].getStatus() + " - HTTP_CREATED" +
                            "\nNo feed!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class GETClient_send implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    private int num;

    GETClient_send(Socket socket, ObjectOutputStream objectOutputStream, int num) {
        this.socket = socket;
        this.objectOutputStream = objectOutputStream ;
        this.num = num;
    }
    @Override
    public void run() {
        try {
            // Send request
            GETClient.updateLogicClock();
//            Scanner input = new Scanner(System.in);
//            System.out.println("Input query, please(enter 0 to exit)");
//            int str = input.nextInt();
            MyFile[] myFiles = {new MyFile(num,"Client", GETClient.logicClock)};
            myFiles[0].setStatus("GET");
            objectOutputStream.writeObject(myFiles);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}