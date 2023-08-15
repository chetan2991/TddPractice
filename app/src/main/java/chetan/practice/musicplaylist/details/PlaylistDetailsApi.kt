package chetan.practice.musicplaylist.details

import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistDetailsApi {
    @GET("playlist-details/{id}")
    suspend fun fetchPlaylistDetails( @Path("id")id: String) : PlaylistDetails
}
