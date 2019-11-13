package com.ace.homework2


import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class Test {

    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val editBtnId = rule.activity.resources.getIdentifier("btnToRest", "id", rule.activity.packageName)
        Espresso.onView(ViewMatchers.withId(editBtnId)).perform(ViewActions.click())
        Thread.sleep(5000)
        Web.onWebView()
            .withElement(findElement(Locator.LINK_TEXT, "Log in"))
            .perform(webClick())
        Thread.sleep(5000)
    }
}
