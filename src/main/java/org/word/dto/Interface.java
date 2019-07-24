package org.word.dto;

import java.util.List;

import lombok.Data;

/**
 * @ClassName Interface
 * @Author zhaohaiwei
 * @Description Interface DTO
 * @Date 10:01 2019-07-19
 * @Version 1.0.0
 */
@Data
public class Interface {

    /**
     * 接口名称
     */
    private String summary;

    /**
     * 标签(所属Api名称)
     */
    private String tag;

    /**
     * url
     */
    private String url;

    /**
     * 功能描述
     */
    private String description;

    /**
     * 请求参数格式
     */
    private String requestForm;

    /**
     * 响应参数格式
     */
    private String responseForm;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 请求参数列表
     */
    private List<RequestParam> requestList;

    // /**
    // * 返回体
    // */
    // private List<Response> responseList;

    /**
     * 请求参数样例
     */
    private String requestParam;

    /**
     * 返回参数样例
     */
    private String responseParam;
}
