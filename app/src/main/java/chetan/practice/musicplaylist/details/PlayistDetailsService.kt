package chetan.practice.musicplaylist.details

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlayistDetailsService @Inject constructor(
    private val api : PlaylistDetailsApi
){
    suspend fun fetchPlaylistDetails(id: String) : Flow<Result<PlaylistDetails>>{
       return flow {
            emit(Result.success(api.fetchPlaylistDetails(id)))
        }.catch {
            emit(Result.failure(RuntimeException(it.message)))
       }

    }

}
