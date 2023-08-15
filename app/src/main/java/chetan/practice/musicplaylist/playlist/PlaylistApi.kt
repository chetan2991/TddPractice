package chetan.practice.musicplaylist.playlist

import retrofit2.http.GET

interface PlaylistApi{

    @GET("playlist")
    suspend fun fetchPlaylist() : List<PlaylistRaw>

}
