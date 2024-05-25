package search;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SearchStrategy<T> {
    List<T> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException;
}
