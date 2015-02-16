import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UserMap {
    private ConcurrentHashMap<String, Color> users;

    private static class Holder {
        static final UserMap INSTANCE = new UserMap();
    }

    public static UserMap getInstance() {
        return Holder.INSTANCE;
    }

    protected UserMap() {
        this.users = new ConcurrentHashMap<String, Color>();
    }

    public boolean hasUser(String name) {
        return users.containsKey(name);
    }

    public String getJson() {
        JsonObjectBuilder usersBuilder = Json.createObjectBuilder();
        for (Map.Entry<String, Color> entry: users.entrySet()) {
            usersBuilder.add(entry.getKey(), entry.getValue().name().toLowerCase());
        }
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("u", usersBuilder);
        return resultBuilder.build().toString();
    }

    public void removeUser(String name) {
        if (hasUser(name))
            this.users.remove(name);
    }

    public void addUser(String name, Color color) {
        this.users.put(name, color);
    }
}
