package chetan.practice.musicplaylist.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val service : PlayistDetailsService
):ViewModel() {
    val playlistDetails: MutableLiveData<Result<PlaylistDetails>> = MutableLiveData()
    val detailsloader = MutableLiveData<Boolean>()

    fun getPlaylistDetails(id: String) {
        viewModelScope.launch {
            detailsloader.postValue(true)
            service.fetchPlaylistDetails(id)
                .onEach {
                    detailsloader.postValue(false)
                }.collect{
                playlistDetails.postValue(it)
            }
        }
    }

}
