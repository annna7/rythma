package utils;

import exceptions.IllegalOperationException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SearchUtils {
    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static Field getFieldByName(Class<?> type, String fieldName) {
        for (Field field : getAllFields(type)) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new IllegalOperationException("Field not found");
    }

    public static <T> List<T> searchByAttribute(List<T> items, String attributeName, String attributeValue) throws NoSuchFieldException, IllegalAccessException {
        List<T> matchedItems = new ArrayList<>();
        for (T item : items) {
            Field field = getFieldByName(item.getClass(), attributeName);
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
            Field field = getFieldByName(item.getClass(), attributeName);
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

    public static <T> List<T> searchInListAttribute(List<T> items, String attributeName, String searchValue) {
        List<T> matchedItems = new ArrayList<>();
        String lowerCaseSearchValue = searchValue.toLowerCase();

        try {
            for (T item : items) {
                Field field = item.getClass().getDeclaredField(attributeName);
                field.setAccessible(true);
                Object fieldValue = field.get(item);

                if (fieldValue instanceof List) {
                    List<?> fieldList = (List<?>) fieldValue;
                    for (Object fieldItem : fieldList) {
                        if (fieldItem.toString().toLowerCase().contains(lowerCaseSearchValue)) {
                            matchedItems.add(item);
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Reflection error when accessing attribute: " + e.getMessage());
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
