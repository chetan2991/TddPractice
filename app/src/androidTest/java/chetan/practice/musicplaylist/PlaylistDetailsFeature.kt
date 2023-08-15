package chetan.practice.musicplaylist

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import org.hamcrest.CoreMatchers
import org.junit.Test
import chetan.practice.musicplaylist.playlist.idelingResource

class PlaylistDetailsFeature : BaseUiTest() {

    @Test
    fun displayPlaylistNameAndDetails(){
        navigateToPlaylistDetails(0)
        assertDisplayed(R.id.playlist_name)
        assertDisplayed(R.id.playlist_details)
        assertDisplayed("Hard Rock Cafe")
        assertDisplayed(
            "Rock your senses with this timeless signature vibe list. \n\n • Poison \n • You shook me all night \n • Zombie \n • Rock'n Me \n • Thunderstruck \n • I Hate Myself for Loving you \n • Crazy \n • Knockin' on Heavens Door"
        )
    }



    @Test
    fun displayLoaderWhileFetchingPlaylistDetails(){
        IdlingRegistry.getInstance().unregister(idelingResource)
        Thread.sleep(3000)
        navigateToPlaylistDetails(0)
        assertDisplayed(R.id.details_loader)

    }

    @Test
    fun hideLoader(){
        navigateToPlaylistDetails(0)
        assertNotDisplayed(R.id.details_loader)
    }

    @Test
    fun displayErrorMessageWhenNetworkFail(){
        navigateToPlaylistDetails(1)
        assertDisplayed(R.string.generic_error)
    }

    @Test
    fun hideErrorMessage(){
        navigateToPlaylistDetails(2)
        Thread.sleep(3000)
        assertNotExist(R.string.generic_error)
    }
    private fun navigateToPlaylistDetails(row : Int) {
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.playlist_image),
                ViewMatchers.isDescendantOfA(nthChildOf(ViewMatchers.withId(R.id.playlist_list), row))
            )
        )
            .perform(ViewActions.click())
    }
}