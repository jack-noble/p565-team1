package com.infinitycare.health;

import com.infinitycare.health.login.SendEmailSMTP;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class HealthApplicationTests {

    @Test
    public void contextLoads() {
        String newBody = "";
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(new Integer(SendEmailSMTP.generateRandomNumber(1000, 9999)) > 999);
            Assert.assertTrue(new Integer(SendEmailSMTP.generateRandomNumber(5000, 9999)) > 4999);
            Assert.assertTrue(new Integer(SendEmailSMTP.generateRandomNumber(0, 9999)) > 0);
        }
    }

}
