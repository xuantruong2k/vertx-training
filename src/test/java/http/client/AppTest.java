/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package http.client;

import org.junit.Test;
import static org.junit.Assert.*;

import main.App;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
