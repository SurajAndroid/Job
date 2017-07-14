package dtos;

/**
 * Created by chauhan on 5/13/2017.
 */

public class SearchEmployeeDTO {

    String condidateName, location, experiece, skilles, employeeImage;

    public String getCondidateName() {
        return condidateName;
    }

    public void setCondidateName(String condidateName) {
        this.condidateName = condidateName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperiece() {
        return experiece;
    }

    public void setExperiece(String experiece) {
        this.experiece = experiece;
    }

    public String getSkilles() {
        return skilles;
    }

    public void setSkilles(String skilles) {
        this.skilles = skilles;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage;
    }
}
