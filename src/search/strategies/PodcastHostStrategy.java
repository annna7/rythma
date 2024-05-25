package search.strategies;

import models.users.Host;
import search.SearchStrategy;
import services.UserService;
import utils.SearchUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PodcastHostStrategy implements SearchStrategy<Host> {
    @Override
    public List<Host> search(Map<String, String> query) throws NoSuchFieldException, IllegalAccessException, SQLException {
        List<Host> hosts = UserService.getInstance().getAllHosts();
        if (query.containsKey("name")) {
            hosts = SearchUtils.searchByAttribute(hosts, "displayName", query.get("name"));
        }
        if (query.containsKey("affiliation")) {
            hosts = SearchUtils.searchByAttribute(hosts, "affiliation", query.get("affiliation"));
        }
        return hosts;
    }
}
