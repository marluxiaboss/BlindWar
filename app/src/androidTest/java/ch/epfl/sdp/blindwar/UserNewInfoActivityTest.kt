package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.login.UserAdditionalInfoActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutionException

@RunWith(AndroidJUnit4::class)
class UserNewInfoActivityTest : TestCase() {

    private val strNotDefault = "notDefaultText"
    private val email = "test@test.test"
    private val password = "testTest"
    private val validPseudo = "validPseudo"

    @get:Rule
    var testRule = ActivityScenarioRule(
        UserNewInfoActivity::class.java
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
    fun testLayoutVisibility() {
        val visibleIds = listOf(
            R.id.NU_pseudo,
            R.id.NU_FirstName,
            R.id.NU_LastName,
            R.id.NU_additional_info,
            R.id.NU_Confirm_Btn
        )
        for (id in visibleIds) {
            onView(withId(id))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }
    }

    @Test
    fun testConfirm_PseudoTooShort() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText(""))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
        clickOn(android.R.string.ok)

    }

    @Test
    fun testConfirm_PseudoIsPseudo() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText("Pseudo"))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
        clickOn(android.R.string.ok)
    }

    fun testAdditionalInfoBtn() {
        onView(withId(R.id.NU_additional_info))
            .perform(click())
        intended(hasComponent(UserAdditionalInfoActivity::class.java.name))
    }

    // =====================================
    // Delete default value when click on it
    @Test
    fun testClearPseudo() {
        val id = R.id.NU_pseudo
        onView(withId(id))
            .perform(
                replaceText("Pseudo"),
                click(),
                click(),
                closeSoftKeyboard()
            ) // TODO: Why should click twice to delete text
        onView(withId(id)).check(matches(withText("")))
    }

    @Test
    fun testClearFirstName() {
        val id = R.id.NU_FirstName
        onView(withId(id))
            .perform(
                replaceText("First Name"),
                click(), click(), closeSoftKeyboard()
            )
        onView(withId(id)).check(matches(withText("")))
    }

    @Test
    fun testClearLastName() {
        val id = R.id.NU_LastName
        closeSoftKeyboard()
        onView(withId(id))
            .perform(
                replaceText("Last Name"),
                click(), click(), closeSoftKeyboard()
            )
        onView(withId(id)).check(matches(withText("")))
    }

    // ========================================
    // Don't delete text when not default value
    @Test
    fun testNotClearPseudo() {
        val id = R.id.NU_pseudo
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    @Test
    fun testNotClearFirstName() {
        val id = R.id.NU_FirstName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    @Test
    fun testNotClearLastName() {
        val id = R.id.NU_LastName
        onView(withId(id))
            .perform(replaceText(strNotDefault), closeSoftKeyboard())
        onView(withId(id)).check(matches(withText(strNotDefault)))
    }

    // check No Default Values:

    @Test
    fun testChooseImage() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val oldPackageName = device.currentPackageName

        onView(withId(R.id.NU_editProfilePicture))
            .perform(click())

        // Press back until we get back to our activity
        var currentPackageName: String
        do {
            device.pressBack()
            currentPackageName = device.currentPackageName
        } while (currentPackageName != oldPackageName)

//        onView(withId(R.id.statsButton))
//            .perform(click())
//        intended(hasComponent(StatisticsActivity::class.java.name))
    }

    @Test
    fun testNewUser() {
        testRule.scenario.onActivity { it.intent.putExtra("newUser", true) }
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText(validPseudo), closeSoftKeyboard())
        clickOn(R.id.NU_Confirm_Btn)
        intended(hasComponent(MainMenuActivity::class.java.name))
    }

    /**
    @Test
    fun testUpdateUser() {
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        testRule.scenario.onActivity { it.intent.putExtra("newUser", false) }
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText(validPseudo))
        clickOn(R.id.NU_Confirm_Btn)
        //intended(hasComponent(MainMenuActivity::class.java.name))
    }**/

    @Test
    fun testAdditionalInfo() {
        clickOn(R.id.NU_additional_info)
        intended(hasComponent(UserAdditionalInfoActivity::class.java.name))
    }

    @Test
    fun testBackButton(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        assertTrue("Back button can't be pressed", device.pressBack())
    }
}