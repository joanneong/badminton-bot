package amateurs.util;

import java.util.List;

public class StringDisplayUtil {
    /**
     * Creates a single string where each element is separated by a comma
     * @param toFormat      list of elements to format
     * @return              string with all elements separated by a comma
     */
    public static String getFormattedList(List<String> toFormat) {
        if (toFormat == null || toFormat.isEmpty()) {
            return "";
        }
        return String.join(", ", toFormat);
    }

    /**
     * Creates a bulleted list for a list of items. If there are fewer items than the max number of items allowed,
     * then there will be default empty indices appended, e.g. 1.   2.   3.
     * @param items         items to put in the list
     * @param maxItems      max number of items supported by the list
     * @return              string with all elements (up to maxItems) in a bulleted list format
     */
    public static String getBulletedList(List<String> items, int maxItems) {
        if (items == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder("\n");
        for (int i = 1; i <= items.size(); i++) {
            sb.append(i).append(". ").append(items.get(i - 1)).append("\n");
        }

        for (int i = items.size() + 1; i <= maxItems; i++) {
            sb.append(i).append(".").append("\n");
        }

        return sb.toString();
    }
}
