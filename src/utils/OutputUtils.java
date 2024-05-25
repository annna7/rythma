package utils;

import java.util.List;
import java.util.stream.Collectors;

public class OutputUtils {
    public static String showCollectionEmptyMessage(String collectionType) {
        return "No " + collectionType + " found.";
    }

    public static void showCollectionMessage(String collectionType, List<?> collection) {
        StringBuilder sb = new StringBuilder();
        if (collection.isEmpty()) {
            sb.append(showCollectionEmptyMessage(collectionType));
        } else {
            sb.append(collectionType.toUpperCase()).append(":\n");
            for (Object item : collection) {
                sb.append(item).append("\n");
            }
        }
        System.out.print(sb.toString());
    }

    public static String getCollectionMessageWithCommas(String collectionType, List<?> collection) {
        if (collection.isEmpty()) {
            return showCollectionEmptyMessage(collectionType);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < collection.size(); i++) {
                sb.append(collection.get(i));
                if (i < collection.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }

}
