package utils;

public class StringUtils {
    public static String trimJsonPathString(final String jsonPathString) {
        return jsonPathString.replaceAll("\\[", "").replaceAll("]", "");
    }
}
