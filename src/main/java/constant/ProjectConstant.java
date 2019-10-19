package main.java.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lei on 2019/10/19.
 */
public class ProjectConstant {
    public final static List<String> PIC_SUFFIX = Arrays.asList("bmp", "jpg", "png", "tif", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp");
    public static Integer page = 1;
    public static String url = "";
    public static String cron = "";
    public static List<Map<String, String>> pageDatas = null;
    public static Integer pageDataSize = -1;
    public static Integer currentIndex = 0;
}
