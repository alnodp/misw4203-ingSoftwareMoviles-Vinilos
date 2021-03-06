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

class ConsultarAlbumTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun consultarAlbumHUTest() {
        val bottomNavigationItemView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.navigation_albumes),
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

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.textView), ViewMatchers.withText("Buscando Am??rica"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Buscando Am??rica")))
        textView.perform(ViewActions.click())

        Thread.sleep(2500)

        val titleView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.ad_name), ViewMatchers.withText("Buscando Am??rica"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )

        titleView.check(ViewAssertions.matches(ViewMatchers.withText("Buscando Am??rica")))

        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        Thread.sleep(2000)

        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(2))
        Thread.sleep(2000)
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