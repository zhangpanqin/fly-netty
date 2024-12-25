package com.mflyyou.springboot.user;

import com.mflyyou.springboot.user.api.request.CreateUserRequest;
import com.mflyyou.springboot.user.api.response.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangpanqin
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;


    @Test
    void create() throws Exception {
        CreateUserResponse response = userController.create(CreateUserRequest.builder()
                .age(1)
                .username("小明")
                .build());
        assertThat(response.getId())
                .isEqualTo(10086L);
    }
}