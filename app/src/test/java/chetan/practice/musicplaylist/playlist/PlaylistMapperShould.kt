package chetan.practice.musicplaylist.playlist

import junit.framework.TestCase.assertEquals
import org.junit.Test
import chetan.practice.musicplaylist.R
import chetan.practice.musicplaylist.utils.BaseUnitTest

class PlaylistMapperShould : BaseUnitTest() {

    private val playlistRaw = PlaylistRaw("1","name","category")
    private val playlistRock = PlaylistRaw("2","name","rock")
    private val mapper = PlaylistMapper()
    private val playlist = mapper(listOf(playlistRaw,playlistRock))
    @Test
    fun keepSameId(){
        assertEquals(playlistRaw.id, playlist.get(0).id)
    }

    @Test
    fun keepSameName(){
        assertEquals(playlistRaw.name, playlist.get(0).name)
    }

    @Test
    fun keepSameCategory(){
        assertEquals(playlistRaw.category, playlist.get(0).category)
    }

    @Test
    fun mapDefaultImageWhenNotRock(){
        assertEquals(R.mipmap.playlist, playlist.get(0).image)
    }

    @Test
    fun rockImageWhenRockCategory(){
        assertEquals(R.mipmap.rock,playlist.get(1).image)
    }
}