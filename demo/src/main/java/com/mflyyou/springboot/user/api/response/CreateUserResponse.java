package com.mflyyou.springboot.user.api.response;

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
public class CreateUserResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
}
