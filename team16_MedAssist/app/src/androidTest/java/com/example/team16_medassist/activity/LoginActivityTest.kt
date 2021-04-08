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
         * closeSoftKeyboard to prevent keyboard from blocking UI
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
        runBlocking{ delay(500) }
        onView(withId(R.id.reportFTextViewTitle)).check(matches(isDisplayed()))
        runBlocking{ delay(500) }
        /**
         * click on the case item in the list
         */
        onView(withId(R.id.imageButtonHistory)).perform(click())
        runBlocking{ delay(500) }
        /**
         * check edit case
         */
        onView((withId(R.id.editCase))).perform(click())
        runBlocking{ delay(500) }

        /**
         * change name
         */
        onView(withId(R.id.editPFTextViewName)).perform(clearText()).perform(typeText("IU"))
        runBlocking{ delay(500) }
        onView(isRoot()).perform(closeSoftKeyboard());
        runBlocking{ delay(500) }
        /**
         * scroll down to the update button and click it
         */
        onView(withId(R.id.updateCaseButton)).perform(scrollTo(), click())
        runBlocking{ delay(500) }

        /**
         * check if the text view is edited
         */
        onView(withId(R.id.editPFTextViewName)).check(matches(withText("IU")))

        /**
         * click diagnosis button
         */
        onView(withId(R.id.startDiagonsisButton)).perform(click())
        runBlocking{ delay(500) }
        /**
         * click the to start voice recognition
         */
        onView((withId(R.id.voicePFImageButton))).perform(click())
        runBlocking{ delay(500) }
        /**
         * stop voice recognition to get diagnosis
         */
        onView(withId(R.id.startDiagonsisButton)).perform(click())
        runBlocking{ delay(500) }
        onView(withId(R.id.textViewPatientCaseDiagnosisDetails)).check(matches(withText("PNEUMOTHORAX")))





        

    }

    @Test
    fun checkCaseHistory() {

        /**
         * find teh log in field and input correct email and pw.
         * click log in button.
         * wait 5 sec to authenticate.
         * if "CASE ID #" is displayed it means log in successful
         * closeSoftKeyboard to prevent keyboard from blocking UI
         */
        onView(withId(R.id.editTextUsername)).perform(typeText("nuwin@gmail.com"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("88888888"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click())
        runBlocking{ delay(5000) }
        onView(withText("CASE ID #")).check(matches(isDisplayed()))

        /**
         * go to case history
         */
        onView(withId(R.id.buttonViewHistory)).perform(click())
    }

    @Test
    fun CheckLoginDoctorPass(){
        /**
         * find teh log in field and input correct email and pw.
         * click log in button.
         * wait 5 sec to authenticate.
         * if "CASE ID #" is displayed it means log in successful
         * closeSoftKeyboard to prevent keyboard from blockin UI
         */
        onView(withId(R.id.editTextUsername)).perform(typeText("doctor@gmail.com"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("00000000"))
        onView(withId(R.id.editTextUsername)).perform(closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click())
        runBlocking{ delay(500) }

    }
}



