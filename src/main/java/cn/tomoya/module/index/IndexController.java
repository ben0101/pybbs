package cn.tomoya.module.index;

import cn.tomoya.common.BaseController;
import cn.tomoya.common.Constants;
import cn.tomoya.interceptor.UserInterceptor;
import cn.tomoya.module.section.Section;
import cn.tomoya.module.topic.Topic;
import cn.tomoya.utils.QiniuUpload;
import cn.tomoya.utils.StrUtil;
import cn.tomoya.utils.ext.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://bbs.tomoya.cn
 */
@ControllerBind(controllerKey = "/", viewPath = "WEB-INF/page")
public class IndexController extends BaseController {

    /**
     * 首页
     */
    public void index() {
        String tab = getPara("tab");
        if (StrUtil.isBlank(tab)) {
            tab = "all";
        }
        if (!tab.equals("all") && !tab.equals("good") && !tab.equals("noreply")) {
            Section section = Section.me.findByTab(tab);
            setAttr("sectionName", section.getStr("name"));
        } else {
            setAttr("sectionName", "版块");
        }
        Page page = Topic.me.page(getParaToInt("p", 1), PropKit.getInt("pageSize", 20), tab);
        setAttr("tab", tab);
        setAttr("sections", Section.me.findAll());
        setAttr("page", page);
        render("index.ftl");
    }

    /**
     * 登录
     */
    public void login() {
        if (getUser() == null) {
            render("login.ftl");
        } else {
            redirect("/");
        }
    }

    /**
     * 登出
     */
    public void logout() {
        removeCookie(Constants.USER_ACCESS_TOKEN, "/", PropKit.get("cookie.domain"));
        redirect("/");
    }

    /**
     * 关于
     */
    public void about() {
        render("about.ftl");
    }

    /**
     * 上传
     */
    @Before(UserInterceptor.class)
    public void upload() {
        try {
            List<UploadFile> uploadFiles = getFiles(PropKit.get("static.path"));
            List<String> urls = new ArrayList<>();
            for(UploadFile uf: uploadFiles) {
                String url = "";
                if(PropKit.get("upload.type").equals("local")) {
                    url = PropKit.get("file.domain") + "/static/upload/" + uf.getFileName();
                    urls.add(url);
                } else if(PropKit.get("upload.type").equals("qiniu")) {
                    // 将本地文件上传到七牛,并删除本地文件
                    String filePath = uf.getUploadPath() + uf.getFileName();
                    Map map = new QiniuUpload().upload(filePath);
                    new File(filePath).delete();
                    url = PropKit.get("qiniu.url") + "/" + map.get("key");
                    urls.add(url);
                }
            }
            success(urls);
        } catch (Exception e) {
            e.printStackTrace();
            error("上传失败");
        }
    }

    /**
     * 积分前100名用户
     */
    public void top100() {
        render("top100.ftl");
    }

    /**
     * 捐赠
     */
    public void donate() {
        render("donate.ftl");
    }

    /**
     * 清理缓存
     */
    @Before({
            UserInterceptor.class,
//            PermissionInterceptor.class
    })
    public void clear() {
//        clearCache(Constants.SECTIONS_CACHE, null);
//        clearCache(Constants.SECTION_CACHE, null);
//        clearCache(Constants.TOPIC_CACHE, null);
//        clearCache(Constants.TOPIC_APPEND_CACHE, null);
//        clearCache(Constants.USERINFO_CACHE, null);
//        clearCache(Constants.USER_TOPICS_CACHE, null);
//        clearCache(Constants.USER_REPLIES_CACHE, null);
//        clearCache(Constants.USER_SCORE_CACHE, null);
//        clearCache(Constants.ROLE_CACHE, null);
//        clearCache(Constants.PERMISSION_CACHE, null);
//        clearCache(Constants.COLLECT_CACHE, null);
//        renderText("clear cache finish!");
    }

}