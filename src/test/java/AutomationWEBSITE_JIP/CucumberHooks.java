package AutomationWEBSITE_JIP;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberHooks {

    @Before
    public static void beforeAll(){
        BaseTest.setupDriver();
        BaseTest.openUrl("https://jrp.folkatech.com");
    }

    @After
    public void afterScenario(Scenario scenario) {

        // ====== JIKA SCENARIO GAGAL ======
        if (scenario.isFailed()) {

            // attach screenshot
            scenario.attach(
                    BaseTest.takeScreenshot(),
                    "image/png",
                    "FAILED_SCREENSHOT"
            );

            // pause 20 detik jika gagal
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // ====== PAUSE SELALU DI AKHIR SCENARIO ======
        try {
            Thread.sleep(10_000); // pause 10 detik (pass / fail)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BaseTest.stopDriver();
    }
}
