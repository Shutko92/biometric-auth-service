package com.pm.biometric_auth_service.service;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Value("${twilio.phone-number}")
    private String twilioNumber;

    @Autowired
    private TwilioRestClient twilioClient;

    public void sendSms(String to, String message) {
        PhoneNumber toNumber = new PhoneNumber(to);
        PhoneNumber fromNumber = new PhoneNumber(twilioNumber);

        Message.creator(toNumber, fromNumber, message)
                .create(twilioClient);
    }
}
