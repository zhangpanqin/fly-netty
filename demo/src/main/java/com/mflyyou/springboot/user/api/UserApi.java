package com.mflyyou.springboot.user.api;

import com.mflyyou.springboot.user.api.request.CreateUserRequest;
import com.mflyyou.springboot.user.api.response.CreateUserResponse;

/**
 * @author zhangpanqin
 */
public interface UserApi {
    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @return 创建用户响应
     */
    CreateUserResponse create(CreateUserRequest request);
}
