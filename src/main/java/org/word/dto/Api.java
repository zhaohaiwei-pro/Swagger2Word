package org.word.dto;

import java.util.List;

import lombok.Data;

/**
 * @ClassName Api
 * @Author zhaohaiwei
 * @Description Api接口类
 * @Date 10:01 2019-07-19
 * @Version 1.0.0
 */
@Data
public class Api {

    /**
     * 名称/标签
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口列表
     */
    private List<Interface> interfaceList;
}
