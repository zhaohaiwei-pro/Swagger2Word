package org.word.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.word.dto.Api;
import org.word.dto.ApiRoot;
import org.word.dto.Interface;
import org.word.dto.Module;
import org.word.dto.RequestParam;
import org.word.dto.ResponseCode;
import org.word.service.WordService;
import org.word.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName WordServiceImpl
 * @Author zhaohaiwei
 * @Description 接口文档生成实现类
 * @Date 11:00 2019-07-19
 * @Version 1.0.0
 */
@Slf4j
@Service
public class WordServiceImpl implements WordService {

    private static final String SUBSTR = "://";

    @Value("${swagger.root.title}")
    private String rootTitle;
    @Value("${swagger.base.project.host}")
    private String host;
    @Value("${swagger.base.project.path}")
    private String path;
    @Value("${swagger.base.json.url}")
    private String swaggerJsonUrl;
    @Value("${swagger.modules}")
    private String modules;
    @Value("${swagger.response.code}")
    private String responseCode;
    @Value("${swagger.response.desc}")
    private String responseDesc;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 自动生成接口文档
     */
    @Override
    public ApiRoot generateApidoc() {
        ApiRoot apiRoot = new ApiRoot();
        apiRoot.setTitle(rootTitle);
        apiRoot.setHost(host);
        apiRoot.setBasePath(path);
        apiRoot.setResponseCodeList(getResponseCode());
        apiRoot.setModuleList(getModuleList());
        return apiRoot;
    }

    private List<ResponseCode> getResponseCode() {
        List<ResponseCode> responseCodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(responseCode)) {
            List<String> codeList = Arrays.asList(responseCode.split(","));
            List<String> descList = new ArrayList<>();
            if (StringUtils.isNotEmpty(responseDesc)) {
                descList = Arrays.asList(responseDesc.split(","));
            }
            for (int i = 0; i < codeList.size(); i++) {
                responseCodeList.add(new ResponseCode(codeList.get(i), descList.get(i)));
            }
        }
        return responseCodeList;
    }

    private List<String> getJsonUrlList() {
        List<String> urlList = new ArrayList<>();
        if (Strings.isNotEmpty(modules)) {
            List<String> moduleList = Arrays.asList(modules.split(","));
            moduleList.forEach(module -> {
                urlList.add(host.concat(path).concat(swaggerJsonUrl).concat(module));
            });
        } else {
            urlList.add(host.concat(path).concat(swaggerJsonUrl).concat("default"));
        }
        return urlList;
    }

    private List<Module> getModuleList() {
        List<Module> moduleList = new ArrayList<>();
        List<String> jsonUrlList = getJsonUrlList();
        jsonUrlList.parallelStream().forEachOrdered(url -> {
            try {
                String jsonStr = restTemplate.getForObject(url, String.class);
                // convert JSON string to Map
                Map<String, Object> map = JsonUtils.readValue(jsonStr, HashMap.class);
                Map<String, LinkedHashMap> info = (LinkedHashMap)map.get("info");
                // 解析tags (API列表)
                List<LinkedHashMap> tags = (ArrayList)map.get("tags");
                // 解析paths (所有接口地址)
                Map<String, LinkedHashMap> paths = (LinkedHashMap)map.get("paths");
                List<Api> apiList = new ArrayList<>();
                if (tags != null && tags.size() > 0) {
                    tags.parallelStream().forEachOrdered(tag -> {
                        Api api = new Api();
                        String apiName = String.valueOf(tag.get("name"));
                        api.setName(apiName);
                        api.setDescription(String.valueOf(tag.get("description")));
                        api.setInterfaceList(getInterfaceList(apiName, paths));
                        apiList.add(api);
                    });
                }
                Module module = new Module();
                module.setTitle(String.valueOf(info.get("title")));
                module.setDescription(String.valueOf(info.get("description")));
                module.setVersion(String.valueOf(info.get("version")));
                module.setApiList(apiList);
                moduleList.add(module);
            } catch (Exception e) {
                log.error("parse error", e);
            }
        });
        return moduleList;
    }

    private List<Interface> getInterfaceList(String apiName, Map<String, LinkedHashMap> paths) {
        List<Interface> interfaceList = new ArrayList<>();
        if (paths != null && paths.size() > 0) {
            paths.forEach((key, obj) -> {
                if (obj != null && obj.size() > 0) {
                    Map<String, LinkedHashMap> interfaceInfo = (LinkedHashMap)obj;
                    interfaceInfo.forEach((requestType, detail) -> {
                        Map<String, LinkedHashMap> details = (LinkedHashMap)detail;
                        String tag = String.valueOf(((List)details.get("tags")).get(0));
                        if (StringUtils.equals(apiName, tag)) {
                            Interface interfaceObj = new Interface();
                            interfaceObj.setSummary(String.valueOf(details.get("summary")));
                            interfaceObj
                                .setDescription(String.valueOf(details.get("description")).replace("\n", "<br />"));
                            interfaceObj.setTag(tag);
                            interfaceObj.setUrl(key);
                            interfaceObj.setRequestType(requestType);
                            // 请求参数格式，类似于 multipart/form-data
                            String requestForm = "";
                            List<String> consumes = (List)details.get("consumes");
                            if (consumes != null && consumes.size() > 0) {
                                for (String consume : consumes) {
                                    requestForm += consume + ",";
                                }
                            }
                            // 返回参数格式，类似于 application/json
                            String responseForm = "";
                            List<String> produces = (List)details.get("produces");
                            if (produces != null && produces.size() > 0) {
                                for (String produce : produces) {
                                    responseForm += produce + ",";
                                }
                            }
                            interfaceObj.setResponseForm(StringUtils.removeEnd(responseForm, ","));
                            interfaceObj.setRequestForm(StringUtils.removeEnd(requestForm, ","));
                            List<LinkedHashMap> parameters = (List)details.get("parameters");
                            interfaceObj.setRequestList(getParams(parameters));
                            interfaceList.add(interfaceObj);
                        }

                    });
                }
            });
        }
        return interfaceList;
    }

    private List<RequestParam> getParams(List<LinkedHashMap> parameters) {
        List<RequestParam> paramList = new ArrayList<>();
        if (parameters != null && parameters.size() > 0) {
            parameters.parallelStream().forEachOrdered(param -> {
                RequestParam requestParam = new RequestParam();
                requestParam.setName(String.valueOf(param.get("name")));
                requestParam.setParamType(String.valueOf(param.get("in")));
                requestParam.setRequire((Boolean)param.get("required"));
                requestParam.setRemark(String.valueOf(param.get("description")));
                paramList.add(requestParam);
            });
        }
        return paramList;
    }
}
