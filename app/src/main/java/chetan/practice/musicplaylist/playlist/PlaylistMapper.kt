package chetan.practice.musicplaylist.playlist

import chetan.practice.musicplaylist.R
import javax.inject.Inject

class PlaylistMapper @Inject constructor(): Function1<List<PlaylistRaw>,List<Playlist>>{
    override fun invoke(playlistRaw: List<PlaylistRaw>): List<Playlist> {
       return playlistRaw.map {
           val image = when(it.category){
               "rock" -> R.mipmap.rock
               else -> R.mipmap.playlist
           }
           Playlist(it.id,it.name,it.category, image)
       }
    }

}
