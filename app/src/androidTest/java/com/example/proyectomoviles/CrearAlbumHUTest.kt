package com.example.proyectomoviles


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CrearAlbumHUTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun crearAlbumHUTest() {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val STRING_LENGTH = 5
        val randomString = (1..STRING_LENGTH)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_albumes),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        Thread.sleep(7000)

        val floatingActionButton = onView(
            allOf(
                withId(R.id.addAlbumButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        Thread.sleep(1000)

        val textInputEditText = onView(
            allOf(
                withId(R.id.nameInputEditText),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("E2E Test$randomString"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.coverInputEditText),
                isDisplayed()
            )
        )
        textInputEditText2.perform(
            replaceText("https://colombobucaramanga.edu.co/wp-content/uploads/2013/06/cd_5_angle.jpg"),
            closeSoftKeyboard()
        )

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.releaseDateInputEditText),
                isDisplayed()
            )
        )
        textInputEditText3.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.releaseDateButton),
                childAtPosition(
                    allOf(
                        withId(R.id.releaseDateSection),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.descriptionInputEditText),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("E2E Test$randomString"), closeSoftKeyboard())

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.genreAutoCompleteTextView),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView.perform(click())

        val materialTextView = onView(withText("Salsa")).inRoot(RootMatchers.isPlatformPopup())

        materialTextView.perform(click())

        val materialAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.recordLabelAutoCompleteTextView),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView2.perform(click())

        val materialTextView2 = onView(withText("Sony Music")).inRoot(RootMatchers.isPlatformPopup())

        materialTextView2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.saveButton),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        Thread.sleep(7000)

        val appCompatEditText = onView(
            allOf(
                withId(R.id.AlbumSearchEt),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("E2E Test$randomString"), closeSoftKeyboard())

        Thread.sleep(1000) //Esperar a que filtre los datos

        val textView = onView(
            allOf(
                withId(R.id.textView), withText("E2E Test$randomString"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )

        textView.check(matches(withText("E2E Test$randomString")))
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
