package com.education.eduprime.service;

import com.education.eduprime.model.User;
import com.education.eduprime.model.UserAccount;
import com.education.eduprime.model.dto.CreateUserAccountDto;
import com.education.eduprime.repository.UserAccountRepository;
import com.education.eduprime.repository.UserRepository;
import com.education.eduprime.utils.ModelMapperUtil;
import com.education.eduprime.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Override
    public UserAccount createNewUserAccount(CreateUserAccountDto userAccountPayload) {
        return mapperCreateNewUserAccount(userAccountPayload);
    }

    private UserAccount mapperCreateNewUserAccount(CreateUserAccountDto createUserAccountDto) {
        UserAccount userAccount = modelMapperUtil
                .modelMapperUtility()
                .map(createUserAccountDto, UserAccount.class);
        User user = userRepository.findById(createUserAccountDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + createUserAccountDto.getUserId()));

        userAccount.setBalances(createUserAccountDto.getBalances());
        userAccount.setUser(user);
        userAccountRepository.save(userAccount);

        return userAccount;
    }
}
