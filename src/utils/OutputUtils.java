package utils;

import java.util.List;

public class OutputUtils {
    public static void showCollectionEmptyMessage(String collectionType) {
        System.out.println("No " + collectionType + " found.");
    }

    public static void showCollectionMessage(String collectionType, List<?> collection) {
        if (collection.isEmpty()) {
            showCollectionEmptyMessage(collectionType);
        } else {
            System.out.println(collectionType.toUpperCase() + ":");
            for (Object item : collection) {
                System.out.println(item);
            }
        }
    }
}
