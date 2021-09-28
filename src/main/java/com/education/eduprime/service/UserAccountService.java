package com.education.eduprime.service;

import com.education.eduprime.model.UserAccount;
import com.education.eduprime.model.dto.CreateUserAccountDto;

public interface UserAccountService {
    UserAccount createNewUserAccount(CreateUserAccountDto userAccountPayload);
}
