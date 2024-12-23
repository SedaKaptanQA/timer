import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;

    public static void initializeReport(String reportName) {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportName);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
    }

    public static ExtentReports getExtent() {
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        if (extent != null) {
            test = extent.createTest(testName);
        }
        return test;
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}