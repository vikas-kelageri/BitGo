import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class TransactionExplorerTest {
    private WebDriver driver;
    private static final String URL = "https://blockstream.info/block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.get(URL);
    }

    @Test
    public void testTransactionSectionHeading() {
        try {
            WebElement transactionHeading = driver.findElement(By.xpath("//div[@class='transactions']/h3"));
            String expectedText = "25 of 2875 Transactions";
            Assert.assertTrue(transactionHeading.getText().contains(expectedText), "Transaction heading validation failed!");
            System.out.println("Test Case 1 Passed: Transaction heading is correct.");
        } catch (NoSuchElementException e) {
            System.err.println("Test Case 1 Failed: Transaction section not found!");
        }
    }

    @Test
    public void testExtractTransactions() {
        try {
            List<WebElement> transactions = driver.findElements(By.xpath("//div[@class='transaction-box']"));

            for (WebElement tx : transactions) {
                // Using a relative XPath to get the transaction hash within each transaction box
                String txHash = tx.findElement(By.xpath(".//div[contains(@class,'txn')]/a")).getText();

                // Relative XPath to find inputs & outputs
                List<WebElement> inputs = tx.findElements(By.xpath(".//div[contains(@class, 'vin-header')]//a"));
                List<WebElement> outputs = tx.findElements(By.xpath(".//div[contains(@class, 'vout-header')]//a"));

                if (inputs.size() == 1 && outputs.size() == 2) {
                    System.out.println(" Transaction Hash (1 input, 2 outputs): " + txHash);
                }
            }
        } catch (NoSuchElementException e) {
            System.err.println("Test Case 2 Failed: Could not locate transaction elements!");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
