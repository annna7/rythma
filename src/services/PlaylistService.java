package services;

public class PlaylistService {
    private static PlaylistService instance = null;
    private PlaylistService() {
    }

    public static PlaylistService getInstance() {
        if (instance == null) {
            instance = new PlaylistService();
        }
        return instance;
    }
}
