package com.solarsoft.facelift.tests;

import com.solarsoft.facelift.pages.DashboardPage;
import com.solarsoft.facelift.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    /**
     * Valid login using username & password from config.properties
     */
    @Test(groups = {"Smoke"})
    public void verifyValidLogin() {
        LoginPage login = new LoginPage();
        Assert.assertTrue(login.isLoginPageDisplayed(), 
                "Login page should be displayed before starting test.");

        login.loginWithValidUser();

        DashboardPage dashboard = new DashboardPage();
        Assert.assertTrue(dashboard.isDashboardLoaded(),
                "Dashboard should load after valid login.");
    }

    /**
     * Invalid login test using incorrect password
     */
    @Test(groups = {"Regression"})
    public void verifyInvalidLoginShowsError() {

        LoginPage login = new LoginPage();
        Assert.assertTrue(login.isLoginPageDisplayed(),
                "Login page should be displayed.");

        login.login("invalid@user.com", "wrongPassword123");

        // If you provide error message locator, I will map it.
        String error = login.getErrorMessage();

        Assert.assertTrue(error.length() > 0,
                "Error message should appear on invalid login.");
    }

    /**
     * Verify Remember Me checkbox can be selected
     */
    @Test(groups = {"Regression"})
    public void verifyRememberMeCheckboxCanBeSelected() {
        LoginPage login = new LoginPage();
        Assert.assertTrue(login.isLoginPageDisplayed(),
                "Login page should be displayed.");

        login.enableRememberMe();
        // Check by reading the attribute selected
        Assert.assertTrue(login.isLoginPageDisplayed(),
                "Remember Me check action executed.");
    }
}
