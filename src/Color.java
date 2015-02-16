import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;

public enum Color {
    BLACK("Чёрный"), RED("Красный"), BLUE("Синий"), GREEN("Зелёный");

    private final String value;

    private Color(final String value) {
        this.value = value;
    }

    public static HashMap<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Color color : values()) {
            map.put(color.name().toLowerCase(), color.toString());
        }
        return map;
    }
    public static String getJson() {
        JsonArrayBuilder colorsBuilder = Json.createArrayBuilder();
        for (Color color : values()) {
            colorsBuilder.add(color.name().toLowerCase());
        }
        return colorsBuilder.build().toString();
    }

    @Override
    public String toString() {
        return this.value;
    }
}
