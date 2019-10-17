package com.infinitycare.health.security;

import org.junit.Assert;
import org.junit.Test;

public class TestTextSecurity {

    @Test
    public void testEncryptAndDecrypt() {
        String encryptedString = TextSecurer.encrypt("vijay");
        String decryptedString = TextSecurer.decrypt(encryptedString);

        Assert.assertNotEquals(encryptedString, decryptedString);
        Assert.assertEquals("vijay", decryptedString);

        encryptedString = TextSecurer.encrypt("vivekjamesmaanvitajack");
        decryptedString = TextSecurer.decrypt(encryptedString);

        Assert.assertNotEquals(encryptedString, decryptedString);
        Assert.assertEquals("vivekjamesmaanvitajack", decryptedString);
    }
}
