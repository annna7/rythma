package utils;

import java.util.List;
import java.util.stream.Collectors;

public class OutputUtils {
    // TODO: MAKE THIS RETURN STRINGS
    public static String showCollectionEmptyMessage(String collectionType) {
        return "No " + collectionType + " found.";
    }

    public static void showCollectionMessage(String collectionType, List<?> collection) {
        if (collection.isEmpty()) {
            System.out.println(showCollectionEmptyMessage(collectionType));
        } else {
            System.out.println(collectionType.toUpperCase() + ":");
            for (Object item : collection) {
                System.out.println(item);
            }
        }
    }

    public static String getCollectionMessageWithCommas(String collectionType, List<?> collection) {
        if (collection.isEmpty()) {
           return showCollectionEmptyMessage(collectionType);
        } else {
            return collection.stream().map(Object::toString).collect(Collectors.joining(", "));
        }
    }
}
