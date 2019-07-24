package org.word.dto;

import lombok.Data;

/**
 * @ClassName RequestParam
 * @Author zhaohaiwei
 * @Description 接口请求参数 DTO
 * @Date 10:53 2019-07-19
 * @Version 1.0.0
 */
@Data
public class RequestParam {

    /**
     * 参数名
     */
    private String name;

    /**
     * 数据类型
     */
    private String type;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数长度
     */
    private int length;

    /**
     * 是否必填
     */
    private Boolean require;

    /**
     * 说明
     */
    private String remark;
}
