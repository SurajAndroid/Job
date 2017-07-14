package dtos;

/**
 * Created by SURAJ on 7/3/2017.
 */

public class NotificationDTO {
    String id, view_id, email, message, datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getView_id() {
        return view_id;
    }

    public void setView_id(String view_id) {
        this.view_id = view_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
