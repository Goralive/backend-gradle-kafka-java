package io.fraud.kafka;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDataFaker {
    public String date() {
        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return date.format(new Date());
    }

    public String source() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public String target() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public String currency() {
        return "EUR";
    }

    public String amount() {
        return "2000";
    }
}
