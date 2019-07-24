package org.word.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName ResponseCode
 * @Author zhaohaiwei
 * @Description 接口返回状态 DTO
 * @Date 10:01 2019-07-19
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
public class ResponseCode {

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String description;
}
