package com.taco.backend_demo.dto.user;

import com.taco.backend_demo.entity.UserInfoEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserInfo extends UserInfoEntity {
    public UserInfo(UserInfoEntity userInfo) {
        super(userInfo);
    }
}
