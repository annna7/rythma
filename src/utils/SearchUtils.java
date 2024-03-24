package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SearchUtils {
    public static <T> List<T> searchByAttribute(List<T> items, String attributeName, String attributeValue) throws NoSuchFieldException, IllegalAccessException {
        List<T> matchedItems = new ArrayList<>();
        for (T item : items) {
            Field field = item.getClass().getDeclaredField(attributeName);
            field.setAccessible(true);
            Object value = field.get(item);
            if (value.toString().toLowerCase().contains(attributeValue.toLowerCase())) {
                matchedItems.add(item);
            }
        }
        return matchedItems;
    }

    public static <T> List<T> searchInSetAttribute(List<T> items, String attributeName, String attributeValue) throws NoSuchFieldException, IllegalAccessException {
        List<T> matchedItems = new ArrayList<>();
        for (T item : items) {
            Field field = item.getClass().getDeclaredField(attributeName);
            field.setAccessible(true);
            Object value = field.get(item);
            if (value instanceof Set) {
                Set<?> valueSet = (Set<?>) value;
                for (Object obj : valueSet) {
                    if (obj.toString().toLowerCase().contains(attributeValue.toLowerCase())) {
                        matchedItems.add(item);
                        break;
                    }
                }
            }
        }
        return matchedItems;
    }

    public static <T> List<T> searchByAttributeUsingFunction(List<T> items, Function<T, String> function, String attributeValue) {
        List<T> matchedItems = new ArrayList<>();
        for (T item : items) {
            if (function.apply(item).toLowerCase().contains(attributeValue.toLowerCase())) {
                matchedItems.add(item);
            }
        }
        return matchedItems;
    }

    public static <T, V> List<T> searchByAttributeEquality(List<T> items, String attributeName, V attributeValue) throws NoSuchFieldException, IllegalAccessException {
        List<T> matchedItems = new ArrayList<>();
        for (T item : items) {
            Field field = item.getClass().getDeclaredField(attributeName);
            field.setAccessible(true);
            Object value = field.get(item);
            if (value.equals(attributeValue)) {
                matchedItems.add(item);
            }
        }
        return matchedItems;
    }

}
