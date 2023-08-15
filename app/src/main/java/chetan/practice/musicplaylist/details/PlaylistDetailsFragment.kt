package chetan.practice.musicplaylist.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist_detail.playlist_details
import kotlinx.android.synthetic.main.fragment_playlist_detail.playlist_details_root
import kotlinx.android.synthetic.main.fragment_playlist_detail.playlist_name
import kotlinx.android.synthetic.main.fragment_playlist_detail.view.details_loader
import chetan.practice.musicplaylist.R
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    lateinit var playlistDetailsViewmodel : PlaylistDetailsViewModel

    @Inject
    lateinit var playlistDetailsViewModelFactory: PlaylistDetailsViewModelFactory

    val args : PlaylistDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_detail, container, false)

        val id = args.playlistId
        setupviewmodel()
        observePlaylistDetails()
        observeLoader(view)
        playlistDetailsViewmodel.getPlaylistDetails(id)
        return view
    }
    private fun observeLoader(view: View) {
        playlistDetailsViewmodel.detailsloader.observe(this as LifecycleOwner, { loading ->
            when (loading) {
                true -> view.details_loader.visibility = View.VISIBLE
                else -> view.details_loader.visibility = View.GONE
            }
        })
    }

    private fun observePlaylistDetails() {
        playlistDetailsViewmodel.playlistDetails.observe(this as LifecycleOwner,
            { playlistDetails ->
                if (playlistDetails.getOrNull() != null) {
                    setupUi(playlistDetails)
                }else{
                    Snackbar.make(playlist_details_root,
                        R.string.generic_error,
                        Snackbar.LENGTH_LONG).show()
                }
            })
    }

    private fun setupUi(playlistDetails: Result<PlaylistDetails>) {
        playlist_name.text = playlistDetails.getOrNull()!!.name!!
        playlist_details.text = playlistDetails.getOrNull()!!.details!!
    }

    private fun setupviewmodel() {
        playlistDetailsViewmodel = ViewModelProvider(
            this,
            playlistDetailsViewModelFactory
        ).get(PlaylistDetailsViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlaylistDetailsFragment()
    }
}