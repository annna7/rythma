package search;

import enums.SearchStrategyEnum;

import java.util.*;

public class SearchContext {
    private SearchStrategy searchStrategy;
    private SearchStrategyEnum searchStrategyType;

    public SearchContext() {}

    public SearchStrategyEnum getStrategyType() {
        return searchStrategyType;
    }

    public void setStrategy(SearchStrategyEnum searchStrategy) {
        this.searchStrategy = searchStrategy.getStrategyFromEnum();
    }

    public List<?> executeSearch(Map<String, String> query) {
        return searchStrategy.search(query);
    }
}
