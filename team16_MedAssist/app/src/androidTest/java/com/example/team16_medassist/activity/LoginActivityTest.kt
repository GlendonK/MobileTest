package com.example.team16_medassist.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.example.team16_medassist.R
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test


class LoginActivityTest {

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkLogin() {
        val mActivityRule = ActivityTestRule<LoginActivity>(
            LoginActivity::class.java
        )

        /**
         * input wrong log in credentials
         */
        onView(withId(R.id.editTextUsername)).perform(typeText("nuwin@gmail.coma"))
        onView(withId(R.id.editTextPassword)).perform(typeText("88888888a"))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withText("signin:fail")).inRoot(
            withDecorView(
                not(
                    `is`(
                        mActivityRule.activity.window.decorView
                    )
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )


        onView(withId(R.id.editTextUsername)).perform(typeText("nuwin@gmail.com"))
        onView(withId(R.id.editTextPassword)).perform(typeText("88888888"))
        onView(withId(R.id.buttonLogin)).perform(click())

    }
}



