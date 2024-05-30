# Rythma

Rythma is a backend service (written in Java) that simulates a music streaming service. It has a CLI interface that allows users to interact with the service. The service is capable of managing users, songs, playlists, podcasts and more.

## Objects
* User
  * Artist
  * Podcast Host
* Playable Item
  * Song
  * Podcast Episode
* Audio Collection
  * Playlist
  * Album
  * Podcast
* Notification

## Features
* **_User Management_**
  * Register User
  * Login/Logout User
* **_As a User_**
  * Create/Delete Playlists
  * Add/Remove Songs from Playlist
  * **Follow** or **unfollow** Playlists
  * View/clear **notifications** (e.g.: when a playlist you follow adds another song)
  * **Search** for Songs/Playlists/Albums/Podcasts/Artists/Podcast Hosts
* _**As an Artist**_ (+ all User features)
  * Create/Delete Albums
  * Add/Remove Songs from Albums
  * Update description & social media links
* _**As a Podcast Host**_ (+ all User features)
  * Create/Delete Podcasts
  * Add/Remove Episodes from Podcasts
  * Update affiliation

## Design Patterns
* **Singleton** - for services like `UserService`, `MusicService` etc.
* **Strategy** - for different `Search` strategies
* **Factory** - for creating `Users` etc.
* **Command** 
  - wraps every request from the CLI
  - provides an elegant way to handle I/O 
  - makes appropriate calls to the service layer and to the audit system