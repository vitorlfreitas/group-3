package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TerminalChatbotTest {

    @Test
    void testMain_runsWithoutException() {
        assertDoesNotThrow(() -> TerminalChatbot.main(new String[]{}));
    }
}