package com.taco.backend_demo;

import com.taco.backend_demo.common.message.MessageResolver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageResolverTest {
    
    @Test
    public void testResolveMessage() {
        // Test basic message resolution
        String message = MessageResolver.resolveMessage("E001");
        assertEquals("Username or password incorrect", message);
        
        // Test message with arguments
        String messageWithArgs = MessageResolver.resolveMessage("E014", "username");
        assertEquals("Field username cannot be empty", messageWithArgs);
        
        // Test unknown message code
        String unknownMessage = MessageResolver.resolveMessage("UNKNOWN");
        assertEquals("UNKNOWN", unknownMessage);
    }
}