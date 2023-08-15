package chetan.practice.musicplaylist.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_list.view.playlist_list
import kotlinx.android.synthetic.main.fragment_item_list.view.spinner
import chetan.practice.musicplaylist.R
import javax.inject.Inject


@AndroidEntryPoint
class PlaylistFragment : Fragment() {
    lateinit var playlistViewModel  : PlaylistViewModel
    @Inject
    lateinit var playlistViewModelFactory : PlaylistViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        setupViewModel()
        observeLoader(view)
        observePlayist(view)

        return view
    }

    private fun observeLoader(view: View) {
        playlistViewModel.loader.observe(this as LifecycleOwner, { loading ->
            when (loading) {
                true -> view.spinner.visibility = View.VISIBLE
                else -> view.spinner.visibility = View.GONE
            }
        })
    }

    private fun observePlayist(view: View) {
        playlistViewModel.playlists.observe(this as LifecycleOwner, { playlist ->
            if (playlist.getOrNull() != null) {
                setupList(view.playlist_list, playlist.getOrNull()!!)

            }
        })
    }

    private fun setupList(
        view: View?,
        playlist: List<Playlist>
    ) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlist,{id->
                run {
                    val action =
                        PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(id)
                    findNavController().navigate(action)
                }
            })
        }
    }

    private fun setupViewModel() {
        playlistViewModel =
            ViewModelProvider(this, playlistViewModelFactory).get(PlaylistViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistFragment()
    }
}