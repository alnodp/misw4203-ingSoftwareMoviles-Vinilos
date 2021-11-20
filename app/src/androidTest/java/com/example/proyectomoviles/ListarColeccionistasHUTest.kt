package com.example.proyectomoviles

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ListarColeccionistasHUTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun ListarColeccionistasHUTest() {
        val bottomNavigationItemView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.navigation_collectors),
                ViewMatchers.withContentDescription("Coleccionistas"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.nav_view),
                        0
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        bottomNavigationItemView.perform(ViewActions.click())

        Thread.sleep(7000) //Esperar a que la API retorne los Artistas

        val recyclerView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.rvCollector),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val appCompatEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.etCollectorSearch),
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
        appCompatEditText2.perform(ViewActions.click())

        val appCompatEditText3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.etCollectorSearch),
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
        appCompatEditText3.perform(
            ViewActions.replaceText("jaime"),
            ViewActions.closeSoftKeyboard()
        )

        Thread.sleep(1000) //Esperar a que se filtren los artistas

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tvCollectortName), ViewMatchers.withText("Jaime Monsalve"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Jaime Monsalve")))
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
}