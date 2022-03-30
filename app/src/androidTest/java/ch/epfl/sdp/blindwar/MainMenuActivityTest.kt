package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.DemoSRActivity
import ch.epfl.sdp.blindwar.ui.MainMenuActivity
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import ch.epfl.sdp.blindwar.ui.SplashScreenActivity
import ch.epfl.sdp.blindwar.ui.solo.SoloMenuActivity
import ch.epfl.sdp.blindwar.ui.tutorial.TutorialActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MainMenuActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testSoloButton() {
        onView(withId(R.id.soloButton))
            .perform(click())
        intended(hasComponent(SoloMenuActivity::class.java.name))
    }

    @Test
    fun testTutorialButton() {
        onView(withId(R.id.tutorialButton))
            .perform(click())
        intended(hasComponent(TutorialActivity::class.java.name))
    }

    @Test
    fun testProfileButton() {
        onView(withId(R.id.profileButton))
            .perform(click())
        intended(hasComponent(ProfileActivity::class.java.name))
    }

    @Test
    fun testLogoutButton() {
        onView(withId(R.id.logoutButton))
            .perform(click())
        intended(hasComponent(SplashScreenActivity::class.java.name))
    }

    @Test
    fun testLaunchSRDemo() {
        onView(withId(R.id.SpeechButton)).perform(click())
        intended(hasComponent(DemoSRActivity::class.java.name))
    }
}