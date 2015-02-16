import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Calendar;
import java.util.Date;

public class ChatMessage {
    private Date date;
    private String message;
    private String user;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public JsonObjectBuilder asJsonObjectBuilder() {
        JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        String hours = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = String.format("%02d", calendar.get(Calendar.MINUTE));

        messageBuilder.add("d", hours + ":" + minutes).add("u", getUser()).add("m", getMessage());
        return messageBuilder;
    }
}
