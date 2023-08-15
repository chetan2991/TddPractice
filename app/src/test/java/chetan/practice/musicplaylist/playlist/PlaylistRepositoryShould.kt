package chetan.practice.musicplaylist.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import chetan.practice.musicplaylist.utils.BaseUnitTest

class PlaylistRepositoryShould : BaseUnitTest() {

    private val service: PlaylistService = mock<PlaylistService>()
    private val playlist = mock<List<Playlist>>()
    private val mapper : PlaylistMapper = mock()
    private val playlistRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistFromService() = runTest {

        val repository = playlistRepository()
        repository.getPlaylists()

        verify(service, times(1)).fetchPlaylist()

    }

    @Test
    fun emitMappedPlaylistFromService() = runTest {

        val repository = playlistRepository()

        assertEquals(playlist, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateError() = runTest {
        val repository = playlistRepository1()
        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper()= runTest {
        val repository = playlistRepository()
        repository.getPlaylists().first()
        verify(mapper, times(1)).invoke(playlistRaw)
    }

    private fun playlistRepository1(): PlaylistRepository = runBlocking {
        whenever(service.fetchPlaylist()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )

        return@runBlocking PlaylistRepository(service, mapper)
    }



    private fun playlistRepository(): PlaylistRepository = runBlocking {
        whenever(service.fetchPlaylist()).thenReturn(
            flow {
                emit(Result.success(playlistRaw))
            }
        )

        whenever(mapper.invoke(playlistRaw)).thenReturn(playlist)
        return@runBlocking PlaylistRepository(service, mapper)
    }
}