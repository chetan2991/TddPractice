package chetan.practice.details

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import chetan.practice.musicplaylist.details.PlayistDetailsService
import chetan.practice.musicplaylist.details.PlaylistDetails
import chetan.practice.musicplaylist.details.PlaylistDetailsApi
import chetan.practice.musicplaylist.utils.BaseUnitTest

class PlaylistDetailsServiceShould : BaseUnitTest() {

    lateinit var playlistDetailsService: PlayistDetailsService
    private val id = "100"
    private val api : PlaylistDetailsApi = mock()
    private val playlistDetails : PlaylistDetails = mock()
    private val exception = RuntimeException("down backend")
        @Test
        fun fetchPlaylistDetailsFromApi() = runTest{
            playlistDetailsService = PlayistDetailsService(api)
            playlistDetailsService.fetchPlaylistDetails(id).single()
            verify(api, times(1)).fetchPlaylistDetails(id)
        }
     @Test
     fun convertValuesToFlowResultAndEmitThem() = runTest{
         mockSuccesfulcase()

         assertEquals(Result.success(playlistDetails),playlistDetailsService.fetchPlaylistDetails(id).first())
     }

    @Test
    fun emmitErrorResultWheneverNetworkFail()= runTest{
        mockerrorCase()
        assertEquals("down backend", playlistDetailsService.fetchPlaylistDetails(id).single().exceptionOrNull()?.message)
    }

    private suspend fun mockerrorCase() {
        whenever(api.fetchPlaylistDetails(id)).thenThrow(exception)
        playlistDetailsService = PlayistDetailsService(api)
    }

    private suspend fun mockSuccesfulcase() {
        whenever(api.fetchPlaylistDetails(id)).thenReturn(
            playlistDetails
        )
        playlistDetailsService = PlayistDetailsService(api)
    }
}