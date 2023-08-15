package chetan.practice.details

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import chetan.practice.musicplaylist.details.PlayistDetailsService
import chetan.practice.musicplaylist.details.PlaylistDetails
import chetan.practice.musicplaylist.details.PlaylistDetailsViewModel
import chetan.practice.musicplaylist.utils.BaseUnitTest
import chetan.practice.musicplaylist.utils.captureValues
import chetan.practice.musicplaylist.utils.getValueForTest

class PlaylistDetailsViewModelShould : BaseUnitTest() {

    lateinit var viewModel : PlaylistDetailsViewModel
    private val id = "1"
    private val service : PlayistDetailsService = mock()
    private val playlistDetails : PlaylistDetails = mock()
    private val expected = Result.success(playlistDetails)
    private val exception = RuntimeException("something went wrong")
    private val error = Result.failure<PlaylistDetails>(exception)
    @Test
    fun getPlaylistDetailsFromService()= runTest{
        mocksuccessfulcase()
        viewModel.getPlaylistDetails(id)

        viewModel.playlistDetails.getValueForTest()
        verify(service, times(1)).fetchPlaylistDetails(id)

    }

    @Test
    fun emmitPlaylistDetailsFromService() = runTest{
        mocksuccessfulcase()
        viewModel.getPlaylistDetails(id)

        assertEquals(expected, viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun emmitErrorWhenServiceFail() = runTest{
        mockErrorCase()
        assertEquals(error,viewModel.playlistDetails.getValueForTest())
    }

    @Test
    fun showLoaderWhileFetching() = runTest {
        mocksuccessfulcase()
        viewModel.detailsloader.captureValues{
            viewModel.getPlaylistDetails(id)

            viewModel.playlistDetails.getValueForTest()
            assertEquals(true,values[0])
        }
    }

    @Test
    fun closeLoaderAfterPaylistDetailsLoad() = runTest {
        mocksuccessfulcase()
        viewModel.detailsloader.captureValues{
            viewModel.getPlaylistDetails(id)
            viewModel.playlistDetails.getValueForTest()
            assertEquals(false,values.last())
        }
    }

    private suspend fun mockErrorCase() {
        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(error)
            }
        )
        viewModel = PlaylistDetailsViewModel(service)
        viewModel.getPlaylistDetails(id)
    }

    private suspend fun mocksuccessfulcase() {
        whenever(service.fetchPlaylistDetails(id)).thenReturn(
            flow {
                emit(expected)
            }
        )
        viewModel = PlaylistDetailsViewModel(service)
    }
}