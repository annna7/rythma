package services;

public class MusicService {
    private static MusicService instance = null;
    private MusicService() {
    }

    public static MusicService getInstance() {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }
}
