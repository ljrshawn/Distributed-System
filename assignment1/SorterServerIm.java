//delete "package assignment1"
//package assignment1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SorterServerIm extends Remote {
    Sorter cStack() throws RemoteException;
}
