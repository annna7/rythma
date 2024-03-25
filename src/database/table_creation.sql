CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL
);

CREATE TABLE Artist (
    ArtistID INT AUTO_INCREMENT PRIMARY KEY,
    Biography TEXT,
    FOREIGN KEY (ArtistID) REFERENCES User(UserID)
);

CREATE TABLE Host (
    HostID INT AUTO_INCREMENT PRIMARY KEY,
    Affiliation VARCHAR(255),
    FOREIGN KEY (HostID) REFERENCES User(UserID)
);

CREATE TABLE AudioCollection (
    CollectionID INT AUTO_INCREMENT PRIMARY KEY,
    OwnerID INT NOT NULL,
    Name VARCHAR(255) NOT NULL,
    FOREIGN KEY (OwnerID) REFERENCES User(UserID)
);

CREATE TABLE Album (
    AlbumID INT AUTO_INCREMENT PRIMARY KEY,
    Label   VARCHAR(255) NOT NULL,
    FOREIGN KEY (AlbumID) REFERENCES AudioCollection (CollectionID)
);

CREATE TABLE Podcast (
    PodcastID INT AUTO_INCREMENT PRIMARY KEY,
    Description TEXT,
    FOREIGN KEY (PodcastID) REFERENCES AudioCollection(CollectionID)
);

CREATE TABLE Playlist (
    PlaylistID INT AUTO_INCREMENT PRIMARY KEY,
    Description TEXT,
    IsPublic BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (PlaylistID) REFERENCES AudioCollection(CollectionID)
);

CREATE TABLE PlayableItem (
   ItemID INT AUTO_INCREMENT PRIMARY KEY,
   Title VARCHAR(255) NOT NULL,
   Length INT NOT NULL CHECK (Length > 0),
   ReleaseDate DATE NOT NULL,
   CollectionID INT NOT NULL,
   FOREIGN KEY (CollectionID) REFERENCES AudioCollection(CollectionID)
);

CREATE TABLE Song (
    SongID INT AUTO_INCREMENT PRIMARY KEY,
    Genres JSON,
    FOREIGN KEY (SongID) REFERENCES PlayableItem(ItemID)
);

CREATE TABLE Episode (
    EpisodeID INT AUTO_INCREMENT PRIMARY KEY,
    EpisodeNumber INT NOT NULL,
    ShowNotes TEXT,
    Guests JSON,
    FOREIGN KEY (EpisodeID) REFERENCES PlayableItem(ItemID)
);

CREATE TABLE PlaylistSongs (
    PlaylistID INT NOT NULL,
    SongID INT NOT NULL,
    FOREIGN KEY (PlaylistID) REFERENCES Playlist(PlaylistID),
    FOREIGN KEY (SongID) REFERENCES Song(SongID),
    PRIMARY KEY (PlaylistID, SongID)
);

CREATE TABLE Notifications (
    NotificationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Message TEXT NOT NULL,
    IsRead BOOLEAN,
    Type ENUM('NEW_SONG_RELEASE', 'NEW_EPISODE', 'NEW_SONG'),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);


