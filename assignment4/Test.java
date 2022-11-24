import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static AtomicInteger meg = new AtomicInteger(0);;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        String[] result = new String[10];

        for (int i = 0; i < 10; i++) {
            meg = new AtomicInteger(0);
            result[i] = ProfileResponse.runTest(args[0]) + "," + meg.get();
        }

//        Thread.sleep(5000);
        long sum = 0;
        int s = 0;
        int time = 1;
        PrintStream console = System.out;
        String path = args[1];
        PrintStream printStream = new PrintStream(path);
        System.setOut(printStream);
        System.out.println("~~~~~~~~~Runtime~~~~~~~~~");
        System.out.println("No.      Runtime   total_m");
        for (String r : result) {
            String[] res = r.split(",");
            System.out.println(time + ":       " + res[0] + "     " + res[1]);

            sum += Long.parseLong(res[0]);
            s += Integer.parseInt(res[1]);

            time++;
        }
        long average = sum/10;
        System.out.println("average:   " + average + "     " + s/10);
        System.setOut(console);
    }
}
