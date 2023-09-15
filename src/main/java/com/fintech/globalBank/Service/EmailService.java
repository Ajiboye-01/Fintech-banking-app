package com.fintech.globalBank.Service;

import com.fintech.globalBank.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
