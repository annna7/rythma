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
  * Login User
  * Logout User
  * Delete User
* **_As a User_**
  * Create/Delete Playlists
  * Add/Remove Songs from Playlist
  * Follow or unfollow Playlists/Artists/Podcast Hosts
  * Search for Songs/Playlists/Albums/Podcasts/Artists/Podcast Hosts
* _**As an Artist**_ (+ all User features)
  * Create/Delete Albums
  * Add/Remove Songs to Albums
  * Update Own Songs Details
  * Add/Remove Songs from Albums
  * Update description & social media links
* _**As a Podcast Host**_ (+ all User features)
  * Create/Delete Podcasts
  * Add/Remove Episodes to Podcasts
  * Add/Remove Episodes from Podcasts
  * Update Own Episodes Details
  * Update affiliation

## Design Patterns
* **Singleton** - for services like `UserService`, `MusicService` etc.
* **Observer** - for `Notification` handling
* **Strategy** - for different `Search` strategies
* **Factory** - for creating `Users` etc.
* **Command** 
  - wraps every request from the CLI
  - provides an elegant way to handle I/O 
  - makes appropriate calls to the service layer, to the database and to the audit system