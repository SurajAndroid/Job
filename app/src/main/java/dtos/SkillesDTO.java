package dtos;

/**
 * Created by SURAJ on 6/29/2017.
 */

public class SkillesDTO {
    String skillesName;
    boolean skillesflage;

    public SkillesDTO(String skillesName, boolean skillesflage) {
        this.skillesflage = skillesflage;
        this.skillesName = skillesName;
    }

    public String getSkillesName() {
        return skillesName;
    }

    public void setSkillesName(String skillesName) {
        this.skillesName = skillesName;
    }

    public boolean isSkillesflage() {
        return skillesflage;
    }

    public void setSkillesflage(boolean skillesflage) {
        this.skillesflage = skillesflage;
    }
}
