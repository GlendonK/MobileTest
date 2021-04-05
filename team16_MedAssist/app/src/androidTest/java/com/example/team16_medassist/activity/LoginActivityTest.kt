package com.example.team16_medassist.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.team16_medassist.R
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLoginParamedicPass() {
        /**
         * find teh log in field and input correct email and pw.
         * click log in button.
         * wait 5 sec to authenticate.
         * if "CASE ID #" is displayed it means log in successful
         * closeSoftKeyboard to prevent keyboard from blockin UI
         */
        onView(withId(R.id.editTextUsername)).perform(typeText("nuwin@gmail.com"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("88888888"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click())
        runBlocking{ delay(5000) }
        onView(withText("CASE ID #")).check(matches(isDisplayed()))

        /**
         * go to the report page
         */
        onView(withId(R.id.buttonViewReport)).perform(click())
        runBlocking{ delay(1000) }
        onView(withId(R.id.reportFTextViewTitle)).check(matches(isDisplayed())).perform(pressBack())
        runBlocking{ delay(1000) }

        /**
         * go to the case details page
         */
        onView(withId(R.id.buttonView)).perform(click())
        runBlocking{ delay(1000) }

        /**
         * go to case history
         */
        onView(withId(R.id.buttonViewHistory)).perform(click())
        runBlocking{ delay(1000) }

        

    }

    @Test
    fun checkLoginDoctorPass() {

        /**
         * find teh log in field and input correct email and pw.
         * click log in button.
         * wait 5 sec to authenticate.
         * if "CASE ID #" is displayed it means log in successful
         * closeSoftKeyboard to prevent keyboard from blockin UI
         */

        onView(withId(R.id.editTextUsername)).perform(typeText("nuwin@gmail.com"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("88888888"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click())
        runBlocking{ delay(5000) }

    }
}



