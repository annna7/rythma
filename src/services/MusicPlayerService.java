package services;

public class MusicPlayerService {
    private static MusicPlayerService instance = null;
    private MusicPlayerService() {
    }

    public static MusicPlayerService getInstance() {
        if (instance == null) {
            instance = new MusicPlayerService();
        }
        return instance;
    }
}
