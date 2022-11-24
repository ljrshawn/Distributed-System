//delete "package assignment1"
//package assignment1;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class SorterClient {
    private SorterClient() {
    }

    public static void main(String[] args) {
        try {
            System.setProperty("java.security.policy", "/java.policy");

            Registry registry = LocateRegistry.getRegistry(null);

            // Single stack
//            Sorter stub = (Sorter) registry.lookup("Sorter");

            // Multiply stack
            SorterServerIm stub_x = (SorterServerIm) registry.lookup("Sorter");
            Sorter stub = stub_x.cStack();

            // Read file
            String path = args[0];
            Scanner input  = new Scanner(new File(path));
            String[] in = new String[0];

            // Using string array to store file information
            while (input.hasNext()){
                String[] in_i = input.nextLine().split(" ");
                int len = in.length + in_i.length;
                String[] tem = new String[len];
                System.arraycopy(in, 0, tem, 0, in.length);
                System.arraycopy(in_i, 0, tem, in.length, in_i.length);
                in = tem;
            }

            // Operate to every number or operator
            int n = in.length;
            if (n != 0) {
                for (int k = 0; k < n; k++) {
                    if (in[k].equals("ascending") || in[k].equals("descending") || in[k].equals("max") ||
                            in[k].equals("min")) {
                        stub.pushOperator(in[k]);
                    } else if (in[k].equals("pop")) {
                        if (!stub.isEmpty()) {
                            int value = stub.pop();
                            System.out.print(value);
                        }
                        while (!stub.isEmpty()) {
                            int value = stub.pop();
                            System.out.print(" " + value);
                        }
                        if (k > 0) {
                            if (in[k-1].equals("pop") || in[k-1].matches("delayPop(.*)")){
                            } else {
                                System.out.println();
                            }
                        }
                    } else if (in[k].equals("isEmpty")) {
                        boolean value = stub.isEmpty();
                        System.out.println(value);
                    } else if (in[k].matches("delayPop(.*)")) {
                        int millis = Integer.parseInt(in[k].substring(in[k].indexOf("(")+1, in[k].indexOf(")")));
                        if (!stub.isEmpty()) {
                            int value = stub.delayPop(millis);
                            System.out.print(value);
                        }
                        while (!stub.isEmpty()) {
                            int value = stub.delayPop(millis);
                            System.out.print(" " + value);
                        }
                        if (k > 0) {
                            if (in[k-1].equals("pop") || in[k-1].matches("delayPop(.*)")){
                            } else {
                                System.out.println();
                            }
                        }
                    } else {
                        stub.pushValue(Integer.parseInt(in[k]));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
