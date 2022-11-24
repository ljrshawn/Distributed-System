//delete "package assignment1"
//package assignment1;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Sorter extends Remote, Serializable {
    void pushValue(int val) throws RemoteException;
    void pushOperator(String operator) throws RemoteException;
    int pop() throws RemoteException;
    boolean isEmpty() throws RemoteException;
    int delayPop(int millis) throws RemoteException;
}