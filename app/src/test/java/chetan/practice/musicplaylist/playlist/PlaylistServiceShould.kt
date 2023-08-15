package chetan.practice.musicplaylist.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import chetan.practice.musicplaylist.utils.BaseUnitTest
import java.lang.RuntimeException

class PlaylistServiceShould  : BaseUnitTest(){

    private lateinit var service  : PlaylistService
    private val api = mock<PlaylistApi>()
    private val playlist = mock<List<PlaylistRaw>>()
    @Test
    fun fetchPlaylistFromApi() = runBlockingTest{
        service = PlaylistService(api)

        service.fetchPlaylist().first()

        verify(api, times(1)).fetchPlaylist()
    }

    @Test
    fun convertValusesToFlowResultAndEmitThem()=  runBlocking {
        mocksetup()
        assertEquals(Result.success(playlist),service.fetchPlaylist().first())
    }

    private suspend fun mocksetup() {
        whenever(api.fetchPlaylist()).thenReturn(
            playlist
        )
        service = PlaylistService(api)
    }

    @Test
    fun emitErrorResultWhenNetworkFail()= runBlockingTest {

        errormocksetup()
        assertEquals("something went wrong",service.fetchPlaylist().first().exceptionOrNull()?.message)
    }

    private suspend fun errormocksetup() {
        whenever(api.fetchPlaylist()).thenThrow(RuntimeException("down backend developer"))
        service = PlaylistService(api)
    }


}