package com.mflyyou.springboot.user;

import com.mflyyou.springboot.user.api.UserApi;
import com.mflyyou.springboot.user.api.request.CreateUserRequest;
import com.mflyyou.springboot.user.api.response.ConfigResponse;
import com.mflyyou.springboot.user.api.response.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final ZpqConfigrutation zpqConfigrutation;

    @Override
    @PostMapping("")
    public CreateUserResponse create(@RequestBody CreateUserRequest request) {
        log.info("Create user request: {}", request);
        return CreateUserResponse.builder()
                .id(10086L)
                .build();
    }

    /**
     * 查询
     */
    @GetMapping("/config")
    public ConfigResponse getConfig() {
        System.out.println("11111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        return ConfigResponse.builder().name(zpqConfigrutation.getName()).age(zpqConfigrutation.getAge()).build();
    }
}
