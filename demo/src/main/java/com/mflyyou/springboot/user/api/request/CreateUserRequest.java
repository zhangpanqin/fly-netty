package com.mflyyou.springboot.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;

/**
 * @author zhangpanqin
 */
@Builder
@Getter
@AllArgsConstructor
public class CreateUserRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private Integer age;
}
