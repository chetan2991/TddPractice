package chetan.practice.musicplaylist.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

import chetan.practice.musicplaylist.utils.BaseUnitTest
import chetan.practice.musicplaylist.utils.captureValues
import chetan.practice.musicplaylist.utils.getValueForTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlaylistViewModelShould : BaseUnitTest() {


    private val repository   = mock<PlaylistRepository>()
    private val playlist = mock<List<Playlist>>()
    private val expected = Result.success(playlist)
    private val exception = RuntimeException("something went wrong")
    @Test
    fun getPlaylistFromRepository(): Unit = runBlockingTest {

        val viewModel = playlistViewModel()


        viewModel.playlists.getValueForTest()
        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emmitPlaylistFromRepository() : Unit = runBlockingTest{

        val viewModel = playlistViewModel()


        assertEquals(expected, viewModel.playlists.getValueForTest())

    }

    @Test
    fun emmitErrorWhenReceiveError(){
        val viewModel = playlistViewModelForErrorCase()

        assertEquals(exception,viewModel.playlists.getValueForTest()!!.exceptionOrNull())

    }

    @Test
    fun showLoaderWhileFetching() = runTest {
        val viewModel = playlistViewModel()
        viewModel.loader.captureValues{
            viewModel.playlists.getValueForTest()
            assertEquals(true,values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runTest{
        val viewModel = playlistViewModel()
        viewModel.loader.captureValues{
            viewModel.playlists.getValueForTest()
            assertEquals(false,values.last())
        }
    }

    @Test
    fun closeLoaderAfterRecieveError() = runBlockingTest{
        val viewModel = playlistViewModelForErrorCase()
        viewModel.loader.captureValues{
            viewModel.playlists.getValueForTest()
            assertEquals(false,values.last())
        }
    }
    private fun playlistViewModel(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }
        return PlaylistViewModel(repository)
    }

    private fun playlistViewModelForErrorCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(Result.failure<List<Playlist>>(exception))
                }
            )
        }
        return PlaylistViewModel(repository)
    }
}