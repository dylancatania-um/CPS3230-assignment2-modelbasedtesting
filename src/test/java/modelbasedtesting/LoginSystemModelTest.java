package modelbasedtesting;

import modelbasedtesting.enums.LoginStates;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Random;

public class LoginSystemModelTest implements FsmModel{

    private LoginSystem systemUnderTest;

    private LoginStates modelState;

    private boolean loggedIn, loggedOut, loginInvalid;

    @Override
    public LoginStates getState() { return modelState; }

    @Override
    public void reset(final boolean b) {
        modelState = LoginStates.LOGIN_PAGE;
        loggedIn = false;
        loggedOut = true;
        loginInvalid = false;
        if(b) {
            systemUnderTest = new LoginSystem();
        }
    }

    public boolean loginGuard() { return getState().equals(LoginStates.LOGIN_PAGE);}
    public @Action void login() {
        systemUnderTest.validLogin();

        loggedIn = true;
        loggedOut = false;

        modelState = LoginStates.ALERTS_PAGE;

        Assert.assertEquals("The models' logout state does not match the SUT's state.", loggedIn, systemUnderTest.isLoggedIn());
        Assert.assertEquals("The models' logout state does not match the SUT's state.", loggedOut, systemUnderTest.isLoggedOut());
    }

    public boolean logoutGuard() { return getState().equals(LoginStates.ALERTS_PAGE);}
    public @Action void logout() {
        systemUnderTest.logOut();

        loggedOut = true;
        loggedIn = false;

        modelState = LoginStates.LOGIN_PAGE;

        Assert.assertEquals("The model's alerts page state does not match the SUT's state.", loggedIn, systemUnderTest.isLoggedIn());
        Assert.assertEquals("The model's alerts page state does not match the SUT's state.", loggedOut, systemUnderTest.isLoggedOut());
    }

    public boolean invalidLoginGuard() { return getState().equals(LoginStates.LOGIN_PAGE);}
    public @Action void invalidLogin() {
        systemUnderTest.invalidLogin();

        loggedOut = true;
        loggedIn = false;
        loginInvalid = true;

        modelState = LoginStates.LOGIN_PAGE;

        Assert.assertEquals("The model's alerts page state does not match the SUT's state.", loggedOut, systemUnderTest.isLoggedOut());
        Assert.assertEquals("The model's alerts page state does not match the SUT's state.", loggedIn, systemUnderTest.isLoggedIn());
        Assert.assertEquals("The model's alerts page state does not match the SUT's state.", loginInvalid, systemUnderTest.isLoginInvalid());
    }

    @Test
    public void LoginSystemModelTestRunner() throws FileNotFoundException {
        final Tester tester = new GreedyTester(new LoginSystemModelTest());
        tester.setRandom(new Random());
        tester.buildGraph();
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(10);
        tester.printCoverage();
    }
}
