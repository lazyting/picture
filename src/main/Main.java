package main;

import main.java.constant.ProjectConstant;
import main.java.scheduler.CronScheduler;
import main.java.utils.EmptyUtil;
import main.java.utils.PropertyUtil;

import java.util.Scanner;

/**
 *
 * Created by Lei on 2019/10/19.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("（都必填，严格按照格式）输入：图片类型-换图时间（s，m，h）-分辨率（长-高）");
        System.out.println("例如：");
        System.out.println("natural-05m-1440-900");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            String sysIn = scanner.next();
            String[] params = sysIn.split("-");
            if (params.length == 4) {
                boolean ifContinue = true;
                for (String s : params) {
                    if (EmptyUtil.isEmpty(s)) {
                        System.out.println("猪脑袋吗？输入错误了,重启软件吧");
                        ifContinue = false;
                        break;
                    }
                }
                if (ifContinue) {
                    String url = PropertyUtil.getProperty("pic_intenet_address");
                    String cron = "";
                    int num = Integer.parseInt(params[1].substring(0, 2));
                    String unit = params[1].substring(2);
                    if (num > 60) {
                        System.out.println("数字不能大于60");
                        return;
                    } else if (num < 0) {
                        System.out.println("数字不能小于0");
                        return;
                    } else if (!("s".equalsIgnoreCase(unit) || "m".equalsIgnoreCase(unit) || "h".equalsIgnoreCase(unit))) {
                        System.out.println("单位不对");
                        return;
                    } else {
                        if ("h".equalsIgnoreCase(unit)) {
                            if (num > 24) {
                                System.out.println("小时不能大于24");
                                return;
                            }
                            cron = "0 0 */" + num + " * * ?";
                        }
                        if ("m".equalsIgnoreCase(unit)) {
                            cron = "0 */" + num + " * * * ?";
                        }
                        if ("s".equalsIgnoreCase(unit)) {
                            cron = "*/" + num + " * * * * ?";
                        }
                    }
//                    System.out.println(cron);
                    url = url.replace("PIC_TYPE", params[0]).replace("SCREENWIDTH", params[2]).replace("SCREENHEIGHT", params[3]);
                    ProjectConstant.url = url;
                    ProjectConstant.cron = cron;
                    try {
                        CronScheduler.task();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("猪脑袋吗？输入错误了,重启软件吧");
            }
        }

    }
}

