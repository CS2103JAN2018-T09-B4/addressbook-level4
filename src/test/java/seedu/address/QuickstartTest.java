package seedu.address;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;


public class QuickstartTest {


    @Test
    public void runTest() throws IOException {

        assertEquals(Quickstart.run(), "No upcoming events found.");
    }
}
