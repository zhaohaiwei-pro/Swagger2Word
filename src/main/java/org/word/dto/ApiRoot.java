package org.word.dto;

import java.util.List;

import lombok.Data;

/**
 * @ClassName ApiRoot
 * @Author zhaohaiwei
 * @Description ApiRoot DTO
 * @Date 10:01 2019-07-19
 * @Version 1.0.0
 */
@Data
public class ApiRoot {

    /**
     * 标题
     */
    private String title;

    /**
     * 访问地址
     */
    private String host;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号
     */
    private String version;

    /**
     * 模块列表
     */
    private List<Module> moduleList;

    /**
     * 状态码列表
     */
    private List<ResponseCode> responseCodeList;

}
