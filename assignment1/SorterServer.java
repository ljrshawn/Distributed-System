//delete "package assignment1"
//package assignment1;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class SorterServer extends UnicastRemoteObject implements SorterServerIm {

    public SorterServer() throws RemoteException {
        super();
        System.setProperty("java.rmi.server.codebase", SorterServerIm.class.getProtectionDomain()
                .getCodeSource().getLocation().toString());

        System.setProperty("java.security.policy", "/java.policy");

        try {
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Sorter", this);
            System.err.println("Server registered. SorterServer ready...");
        } catch (Exception e) {
            System.err.println("Error registering server: "
                    + e.getMessage());
        }
    }

    // Creat stack for different client
    public Sorter cStack() throws RemoteException {
        return new SorterImplementation();
    }

    public static void main(String[] args) {

        try {
            new SorterServer();
//            SorterImplementation obj = new SorterImplementation();
//            Sorter stub = (Sorter) UnicastRemoteObject.exportObject(obj, 0);
//
//            // Bind the remote object's stub in the registry
//            Registry registry = LocateRegistry.getRegistry();
//            registry.bind("Sorter", stub);

//            System.err.println("SorterServer ready");
        } catch (Exception e) {
            System.err.println("SorterServer exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
