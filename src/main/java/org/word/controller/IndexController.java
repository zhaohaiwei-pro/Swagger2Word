package org.word.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.word.dto.ApiRoot;
import org.word.service.WordService;

/**
 * @ClassName IndexController
 * @Author zhaohaiwei
 * @Description 生成API接口word文档
 * @Date 17:27 2019-07-17
 * @Version 1.0.0
 */
@Controller
public class IndexController {

    @Autowired
    private WordService wordService;

    /**
     * @Author zhaohaiwei
     * @Description 生成apidoc
     * @Date 11:05 2019-07-19
     * @Param model
     * @Return
     */
    @RequestMapping("/apidoc")
    public String apidoc(Model model) {
        ApiRoot apiRoot = wordService.generateApidoc();
        model.addAttribute("apiRoot", apiRoot);
        return "word";
    }
}
