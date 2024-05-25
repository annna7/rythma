package commands.concrete.users;

import commands.Command;
import models.users.User;
import search.SearchContext;
import enums.SearchStrategyEnum;

import java.util.*;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;


public class SearchCommand implements Command {
    public static final Map<SearchStrategyEnum, Set<String>> validFieldsPerType = new HashMap<>() {{
        put(SearchStrategyEnum.SONG, new HashSet<>(Arrays.asList("title", "genre", "album", "artist")));
        put(SearchStrategyEnum.ALBUM, new HashSet<>(Arrays.asList("name", "artist", "label")));
        put(SearchStrategyEnum.ARTIST, new HashSet<>(Arrays.asList("name", "biography")));
        put(SearchStrategyEnum.HOST, new HashSet<>(Arrays.asList("name", "affiliation")));
        put(SearchStrategyEnum.PODCAST, new HashSet<>(Arrays.asList("title", "host")));
        put(SearchStrategyEnum.PODCAST_EPISODE, new HashSet<>(Arrays.asList("title", "podcast", "number", "host", "guests")));
        put(SearchStrategyEnum.PLAYLIST, new HashSet<>(Arrays.asList("name", "owner", "description")));
    }};

    private final SearchContext searchContext = new SearchContext();
    @Override
    public void execute() {
        System.out.println("1. Search for a song\n2. Search for an album\n3. Search for an artist\n4. Search for a podcast\n5. Search for a playlist\n6. Search for a podcast episode\n7. Search for a host\n");
        int option = askForField("your option", Integer::parseInt);

        switch (option) {
            case 1 -> {
                searchContext.setStrategy(SearchStrategyEnum.SONG);
            }
            case 2 -> {
                searchContext.setStrategy(SearchStrategyEnum.ALBUM);
            }
            case 3 -> {
                searchContext.setStrategy(SearchStrategyEnum.ARTIST);
            }
            case 4 -> {
                searchContext.setStrategy(SearchStrategyEnum.PODCAST);
            }
            case 5 -> {
                searchContext.setStrategy(SearchStrategyEnum.PLAYLIST);
            }
            case 6 -> {
                searchContext.setStrategy(SearchStrategyEnum.PODCAST_EPISODE);
            }
            case 7 -> {
                searchContext.setStrategy(SearchStrategyEnum.HOST);
            }
            default -> {
                throw new IllegalArgumentException("Invalid search type");
            }
        }

        showSearchFields(searchContext.getStrategyType());
        String query = askForField("your query", String::valueOf);
        Map<String, String> parsedQuery = parseQuery(query);
        List<?> searchResults = searchContext.executeSearch(parsedQuery);
        searchResults.forEach(System.out::println);
    }

    private void showSearchFields(SearchStrategyEnum searchStrategy) {
        System.out.println("You can search by the following fields: " + String.join(", ", validFieldsPerType.get(searchStrategy)));
        System.out.println("Query example: title: 'Bohemian Rhapsody', genre: 'rock'");
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> parsedQuery = new HashMap<>();
        String[] queryParts = query.split(",");
        for (String queryPart : queryParts) {
            String[] keyValue = queryPart.split(":");
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Invalid query format");
            }
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            if (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'') {
                value = value.substring(1, value.length() - 1);
            }
            if (!validFieldsPerType.get(searchContext.getStrategyType()).contains(key)) {
                throw new IllegalArgumentException("Invalid field: " + key);
            }
            parsedQuery.put(key, value);
        }
        return parsedQuery;
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Search";
    }

    @Override
    public String getCommandDescription() {
        return "Search for a song, album, artist, podcast, playlist, podcast episode, or podcast host";
    }
}
