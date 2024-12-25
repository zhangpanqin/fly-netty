package com.mflyyou.springboot.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;

/**
 * @author zhangpanqin
 */
@Getter
@Builder
@AllArgsConstructor
public class ConfigResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer age;
}
