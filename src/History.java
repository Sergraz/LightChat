import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.concurrent.ConcurrentLinkedQueue;

public class History {
    private final static int HISTORY_SIZE = 10;
    private ConcurrentLinkedQueue<ChatMessage> messages;

    private static class Holder {
        static final History INSTANCE = new History();
    }

    public static History getInstance() {
        return Holder.INSTANCE;
    }

    protected History() {
        this.messages = new ConcurrentLinkedQueue<ChatMessage>();
    }

    public String getMessages() {
        JsonArrayBuilder messagesBuilder = Json.createArrayBuilder();
        for (ChatMessage message : messages) {
            messagesBuilder.add(message.asJsonObjectBuilder());
        }
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("h", messagesBuilder);
        return resultBuilder.build().toString();
    }

    public void putMessage(ChatMessage message) {
        if (this.messages.size() > HISTORY_SIZE) {
            this.messages.poll();
        }
        this.messages.add(message);
    }
}
