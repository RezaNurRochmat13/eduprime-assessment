package com.education.eduprime.presenter;

import com.education.eduprime.model.UserAccount;
import com.education.eduprime.model.dto.CreateUserAccountDto;
import com.education.eduprime.service.UserAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class UserAccountPresenter {
    @Autowired
    private UserAccountServiceImpl userAccountService;

    @PostMapping("/user-accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createNewUserAccount(@RequestBody CreateUserAccountDto payload) {
        Map<String, Object> map = new HashMap<>();
        UserAccount userAccount = userAccountService.createNewUserAccount(payload);

        map.put("data", userAccount);
        return map;
    }
}
