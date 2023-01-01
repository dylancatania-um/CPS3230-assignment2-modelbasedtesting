package modelbasedtesting;

import modelbasedtesting.enums.AlertStates;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class AlertSystemModelTest implements FsmModel {

    private AlertSystem systemUnderTest;

    private AlertStates modelState;

    private boolean alertCreated, alertsDeleted, alertsViewed, alertCreationFailed, alertsDeletionFailed;

    @Override
    public AlertStates getState() { return modelState; }

    @Override
    public void reset(final boolean b) {
        modelState = AlertStates.ALERTS_PAGE;
        alertCreated = false;
        alertsDeleted = false;
        alertsViewed = true;
        alertCreationFailed = false;
        alertsDeletionFailed = false;
        if(b) {
            systemUnderTest = new AlertSystem();
        }
    }

    public boolean createAlertGuard() { return (getState().equals(AlertStates.ALERTS_PAGE) || getState().equals(AlertStates.BAD_API_CALL) || getState().equals(AlertStates.API_CALL)); }
    public @Action void createAlert() throws IOException {
        systemUnderTest.createAlert();

        alertCreated = true;
        alertCreationFailed = false;
        alertsDeletionFailed = false;
//        numberOfAlerts+=1;

        modelState = AlertStates.API_CALL;

        Assert.assertEquals("The models' create alert state doesn't match the SUT's state.", alertCreated, systemUnderTest.isAlertCreated());
    }

    public boolean deleteAlertGuard() { return (getState().equals(AlertStates.ALERTS_PAGE) || getState().equals(AlertStates.BAD_API_CALL) || getState().equals(AlertStates.API_CALL)); }
    public @Action void deleteAlert() throws IOException {
        systemUnderTest.deleteAlerts();

        alertsDeleted = true;
        alertCreationFailed = false;
        alertsDeletionFailed = false;

        modelState = AlertStates.API_CALL;

        Assert.assertEquals("The models' delete alert state doesn't match the SUT's state.", alertsDeleted, systemUnderTest.areAlertsDeleted());
    }

    //either create or delete
    public boolean viewAlertsGuard() { return (getState().equals(AlertStates.ALERTS_PAGE) || getState().equals(AlertStates.BAD_API_CALL) || getState().equals(AlertStates.API_CALL)); }
    public @Action void viewAlerts() {
        systemUnderTest.viewAlerts();

        alertsViewed = true;
        alertCreationFailed = false;
        alertsDeletionFailed = false;

        Assert.assertEquals("The models' view alert state doesn't match the SUT's state.", alertsViewed, systemUnderTest.areAlertsViewed());
    }

    public boolean alertCreationFailedGuard() { return (getState().equals(AlertStates.ALERTS_PAGE) || getState().equals(AlertStates.BAD_API_CALL) || getState().equals(AlertStates.API_CALL));}
    public @Action void alertCreationFailed() throws IOException {
        systemUnderTest.createAlertInvalid();

        alertCreationFailed = true;

        modelState = AlertStates.BAD_API_CALL;

        Assert.assertEquals("The models' failed alert creation state doesn't match the SUT's state.", alertCreationFailed, systemUnderTest.isAlertCreationFailed());

    }

    public boolean alertsDeletionFailedGuard() { return (getState().equals(AlertStates.ALERTS_PAGE) || getState().equals(AlertStates.BAD_API_CALL) || getState().equals(AlertStates.API_CALL)); }
    public @Action void alertsDeletionFailed() throws IOException {
        systemUnderTest.deleteAlertsInvalid();

        alertsDeletionFailed = true;

        modelState = AlertStates.BAD_API_CALL;

        Assert.assertEquals("The models' failed deletion alert state doesn't match the SUT's state.", alertsDeletionFailed, systemUnderTest.areAlertsDeletionFailed());

    }

    @Test
    public void AlertSystemModelRunner() throws FileNotFoundException {
        final Tester tester = new LookaheadTester(new AlertSystemModelTest());
        tester.setRandom(new Random());
        tester.buildGraph();
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(50);
        tester.printCoverage();
    }
}
