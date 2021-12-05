package com.example.proyectomoviles

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test

class AsociarTrackparaAlbumTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun consultarAlbumHUTest() {
        val bottomNavigationItemView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.navigation_albumes),
                ViewMatchers.withContentDescription("Albumes"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavigationItemView.perform(ViewActions.click())

        Thread.sleep(7000) //Esperar a que datos de la API carguen

        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.albumesRV),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val appCompatEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.AlbumSearchEt),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.FrameLayout")),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )

        onView(withId(R.id.AlbumSearchEt)).perform(typeText("Meejo"))
        Thread.sleep(1000)

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.textView), ViewMatchers.withText("Meejo"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Meejo")))
        textView.perform(ViewActions.click())

        Thread.sleep(2000)

        val titleView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.ad_name), ViewMatchers.withText("Meejo"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )

        titleView.check(ViewAssertions.matches(ViewMatchers.withText("Meejo")))

        Thread.sleep(1500)
        onView(withId(R.id.addAlbumTrackButton)).perform(click())
        Thread.sleep(1500)

        val randomName = "Track ${(0..9999).random()}"
        onView(withId(R.id.nameInputEditText)).perform(typeText(randomName))
        onView(withId(R.id.minuteInputEditText)).perform(typeText("%02d".format((1..3).random())))
        onView(withId(R.id.secondInputEditText)).perform(typeText("%02d".format((1..59).random())), closeSoftKeyboard())
        Thread.sleep(1500)

        onView(withId(R.id.saveButton)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.tracksRV)).perform(swipeUp())
        Thread.sleep(1500)

        val trackView = Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.td_name), ViewMatchers.withText(randomName),))
        trackView.check(ViewAssertions.matches(ViewMatchers.withText(randomName)))

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}