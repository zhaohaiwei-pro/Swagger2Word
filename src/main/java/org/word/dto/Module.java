package org.word.dto;

import java.util.List;

import lombok.Data;

/**
 * @ClassName Module
 * @Author zhaohaiwei
 * @Description Module DTO
 * @Date 10:01 2019-07-19
 * @Version 1.0.0
 */
@Data
public class Module {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号
     */
    private String version;

    /**
     * API列表
     */
    private List<Api> apiList;

}
