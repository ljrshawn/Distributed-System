//delete "package assignment1"
//package assignment1;

import java.rmi.RemoteException;
import java.util.Stack;
import java.lang.Thread;

public class SorterImplementation implements Sorter{
    public Stack<Integer> data;

    public SorterImplementation() throws RemoteException {
        data = new Stack<Integer>();
    }

    public void pushValue(int val) {
        data.push(val);
//        System.out.println(val);
    }

    public int pop() {
        if (!isEmpty()) {
            return data.pop();
        } else {
            return -2147483648;
        }
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int delayPop(int millis) {

        // Sleep 0.001*millis seconds
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pop();
    }

    public void pushOperator(String operator) {
        if (!data.empty()) {
            int n = data.size();
            int[] value = new int[n];
            for (int i = 0; i < n; i++) {
                value[i] = data.pop();
            }
            if (operator.equals("ascending") || operator.equals("descending")) {
                sort(value, operator);
            } else if (operator.equals("max") || operator.equals("min")) {
                data_extreme(value, operator);
            }
        }
    }

    private void data_extreme(int[] arr, String operator) {
        int n = arr.length;
        int mum = arr[0];
        for (int i = 1; i < n; i++) {
            if (operator.equals("max")) {
                if (mum < arr[i]) {
                    mum = arr[i];
                }
            } else {
                if (mum > arr[i]) {
                    mum = arr[i];
                }
            }
        }
        data.push(mum);
    }

    // Sort method
    private void sort(int[] arr, String operator) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (operator.equals("descending")) {
                    if (arr[j] > arr[j+1]) {
                        this.swap(arr, j, j+1);
                    }
                }
                else {
                    if (arr[j] < arr[j+1]) {
                        this.swap(arr, j, j+1);
                    }
                }
            }
        }
        for (int i : arr) {
            data.push(i);
        }
    }

    private void swap(int[] arr,int i, int j) {
        int tem = arr[i];
        arr[i] = arr[j];
        arr[j] = tem;
    }
}
