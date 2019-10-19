package main.java.scheduler;

import main.java.constant.ProjectConstant;
import main.java.utils.EmptyUtil;
import main.java.utils.GetPageUtils;
import main.java.utils.JSoupUtil;
import main.java.utils.PropertyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lei on 2019/10/19.
 */
public class UrlHandle {
    public static String getTruthUrl() {
        String thuthUrl = "";
        if (EmptyUtil.isEmpty(ProjectConstant.pageDatas)) {
            ProjectConstant.pageDatas = new ArrayList<>();
            String url = ProjectConstant.url + ProjectConstant.page;
            String page = GetPageUtils.getPageNoUseIp(url);
            JSoupUtil.getElementsByTag(page, "img").stream().forEach((element) -> {
                Map<String, String> pageData = new HashMap<>();
                String eleStr = element.toString();
                pageData.put("number", eleStr.substring(eleStr.lastIndexOf("/") + 1, eleStr.lastIndexOf("__")).split("-")[1]);
                pageData.put("tpye", element.attr("alt").toLowerCase().replace(", ", "-"));
                ProjectConstant.pageDatas.add(pageData);
            });
        }
        String truthAddressSuffix = PropertyUtil.getProperty("truthAddressSuffix");
        if (ProjectConstant.pageDataSize == -1) {
            ProjectConstant.pageDataSize = ProjectConstant.pageDatas.size();
        }
        if (ProjectConstant.currentIndex < ProjectConstant.pageDataSize) {
            String tempPath = truthAddressSuffix + ProjectConstant.pageDatas.get(ProjectConstant.currentIndex).get("tpye") + "-" + ProjectConstant.pageDatas.get(ProjectConstant.currentIndex).get("number");
            ProjectConstant.currentIndex += 1;
            String page = GetPageUtils.getPageNoUseIp(tempPath);
            thuthUrl = JSoupUtil.getElementsByAttributeValue(page, "itemprop", "contentURL").attr("srcset").toString().split(", ")[1].split(" ")[0];
        } else {
            ProjectConstant.pageDataSize = -1;
            ProjectConstant.currentIndex = 0;
            ProjectConstant.pageDatas = null;
            ProjectConstant.page += 1;
            getTruthUrl();
        }
        return thuthUrl;
    }
}
