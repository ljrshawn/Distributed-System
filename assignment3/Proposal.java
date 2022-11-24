import java.io.Serializable;

public class Proposal implements Serializable {
    private int serialNumber;
    private String value;
    private String status = null;

    public Proposal() {
        this.serialNumber = -1;
        this.value = null;
    }

    public Proposal(int serialNumber, String value) {
        this.serialNumber = serialNumber;
        this.value = value;
    }


    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
