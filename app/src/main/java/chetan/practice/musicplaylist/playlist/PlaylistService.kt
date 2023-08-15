package chetan.practice.musicplaylist.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaylistService @Inject constructor(
    private val playlistApi: PlaylistApi
) {

    suspend fun fetchPlaylist(): Flow<Result<List<PlaylistRaw>>> {

        return flow {
            val playlist = playlistApi.fetchPlaylist()
            emit(Result.success(playlist))
        }.catch {
            emit(Result.failure(RuntimeException("something went wrong")))
        }
    }

}
