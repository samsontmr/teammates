package teammates.client.scripts.scalabilitytests;

import org.testng.annotations.Test;
import teammates.common.util.AppUrl;
import teammates.common.util.Const;
import teammates.common.util.Logger;
import teammates.client.scripts.util.Stopwatch;
import teammates.test.cases.browsertests.BaseUiTestCase;
import teammates.test.driver.Priority;
import teammates.test.pageobjects.InstructorFeedbackResultsPage;


/**
 * Tests 'Feedback Results' view of instructors. Run 'InstructorFeedbackResultsPageScaleTestDataGenerator.py' before
 * running the tests.
 * SUT: {@link InstructorFeedbackResultsPage}.
 */
@Priority(-1)
public class InstructorFeedbackResultsPageScaleTest extends BaseUiTestCase {

    private static final String DATA_FOLDER_PATH = "src/client/java/teammates/client/scripts/scalabilitytests/data/";
    private Logger logger = Logger.getLogger();

    @Override
    protected void prepareTestData() {
        // the actual test data is refreshed before each test method
    }

    private void refreshTestData(int numStudents, int numQuestions) {
        refreshTestData(
                "InstructorFeedbackResultsPageScaleTest-" + numStudents + "Students" + numQuestions + "Questions.json");
    }

    private void refreshTestData(String filename) {
        testData = loadDataBundle(DATA_FOLDER_PATH + filename);
        removeAndRestoreDataBundle(testData);
    }

    @Test
    public void testContentWithIncreasingLoad() throws Exception {
        int[] studentLoads = {10, 20};
        int[] questionLoads = {1, 5, 10};

        for (int studentLoad : studentLoads) {
            for (int questionLoad : questionLoads) {
                logger.info("Testing with " + studentLoad + " students, " + questionLoad + " questions...");
                refreshTestData(studentLoad, questionLoad);
                testContent();
            }
        }
    }

    private void testContent() throws Exception {
        InstructorFeedbackResultsPage resultsPage =
                loginToInstructorFeedbackResultsPage("CFResultsUiT.instr", "Open Session");
        resultsPage.waitForPanelsToExpand();
    }

    private InstructorFeedbackResultsPage loginToInstructorFeedbackResultsPage(String instructorName, String fsName) {
        AppUrl resultsUrl =
                createUrl(Const.ActionURIs.INSTRUCTOR_FEEDBACK_RESULTS_PAGE)
                        .withUserId(testData.instructors.get(instructorName).googleId)
                        .withCourseId(testData.feedbackSessions.get(fsName).getCourseId())
                        .withSessionName(testData.feedbackSessions.get(fsName).getFeedbackSessionName());

        InstructorFeedbackResultsPage resultsPage =
                loginAdminToPage(resultsUrl, InstructorFeedbackResultsPage.class);

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        resultsPage.waitForPageToLoad();
        stopwatch.logTimeElapsedInSeconds(logger);

        return resultsPage;
    }
}