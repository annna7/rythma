package utils;

import java.util.ArrayList;

public class OutputUtils {
    public static void showCollectionEmptyMessage(String collectionType) {
        System.out.println("No " + collectionType + " found.");
    }

    public static void showCollectionMessage(String collectionType, ArrayList<?> collection) {
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
