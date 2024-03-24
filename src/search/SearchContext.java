package search;

import enums.SearchStrategyEnum;
import exceptions.IllegalOperationException;

import java.util.*;

public class SearchContext {
    private SearchStrategy<?> searchStrategy;
    private SearchStrategyEnum searchStrategyType;

    public SearchContext() {}

    public SearchStrategyEnum getStrategyType() {
        return searchStrategyType;
    }

    public void setStrategy(SearchStrategyEnum searchStrategy) {
        this.searchStrategyType = searchStrategy;
        this.searchStrategy = searchStrategy.getStrategyFromEnum();
    }

    public List<?> executeSearch(Map<String, String> query) {
        try {
            return searchStrategy.search(query);
        } catch (Exception e) {
            throw new IllegalOperationException("Search failed");
        }
    }
}
