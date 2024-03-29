package main.java.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    static URL url = FileUtils.class.getClassLoader().getResource("ip.txt");

    /**
     * 绝对路径
     *
     * @param fileName
     * @return
     */
    public static String getFileRelativePath(String fileName) {
        System.out.println(FileUtils.class.getClassLoader().getResource(fileName));
        System.out.println(FileUtils.class.getClassLoader().getResource(fileName).getPath());
        return FileUtils.class.getClassLoader().getResource(fileName).getPath();
    }

    /**
     * 获取的ip存入到文件
     *
     * @param object
     * @throws IOException
     */
    public static void writeFile(Object object, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        if (object instanceof List) {
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            try {
                osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                bw = new BufferedWriter(osw);
                for (Map<String, Object> map : (List<Map<String, Object>>) object) {
                    bw.write(map.get("ip") + ":" + map.get("port") + "\r\n");
                }
            } finally {
                close(null, null, null, bw);
                close(null, null, null, osw);
            }
        }
    }

    public static void downloadPicture(String filePath, String index, String name) {
        URL url = null;
        try {
            url = new URL(filePath);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            //System.getProperty("user.dir") 获取当前程序路径
            File file = new File(PropertyUtil.getProperty("file_save_path") + "\\" + index + "\\" + name);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getPict(String address, String dirName, String fileName) throws Exception {
        HttpURLConnection conn = HttpUtil.getConnection(address);
        // 通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        // new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(PropertyUtil.getProperty("file_save_path") + "\\" + dirName + "\\" + fileName);
        File parentFile = imageFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        // 创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        // 写入数据
        outStream.write(data);
        // 关闭输出流
        outStream.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 下载视频
     *
     * @param httpUrl
     * @param saveToFilePath
     */
    public static void downloadVideo(String httpUrl, String saveToFilePath) {
        if (StringUtils.isEmpty(httpUrl) || StringUtils.isEmpty(saveToFilePath)) {
            throw new RuntimeException("httpUrl or saveToFilePath is null...");
        }
        // 1.下载网络文件
        int byteRead;
        InputStream inStream = null;
        FileOutputStream fos = null;

        try {
            //2.获取链接
            URLConnection conn = HttpUtil.getConnection(httpUrl);
            //3.输入流
            inStream = conn.getInputStream();
            //3.写入文件
            fos = new FileOutputStream(dealFileSavePath(httpUrl, saveToFilePath));

            byte[] bs = new byte[1024];
            while ((byteRead = inStream.read(bs)) != -1) {
                fos.write(bs, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inStream, fos, null, null);
        }
    }

    /**
     * 处理保存路径
     *
     * @param url
     * @param saveToFilePath
     * @return
     */
    public static String dealFileSavePath(String url, String saveToFilePath) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(saveToFilePath)) {
            throw new RuntimeException("url or saveToFilePath is null...");
        }
        if (saveToFilePath.contains(".")) {
            String fileNameSuffix = url.substring(url.lastIndexOf("."));
            return saveToFilePath.substring(0, saveToFilePath.lastIndexOf(".")) + fileNameSuffix;
        } else {
            return saveToFilePath + url.substring(url.lastIndexOf("/"));
        }

    }


    /**
     * 追加信息到文件
     *
     * @param fileName
     * @param info
     */
    public static void appendInfoToFile(String fileName, String info) {
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            info = info + System.getProperty("line.separator");
            fileWriter.write(info);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 清空已有的文件内容，以便下次重新写入新的内容
     *
     * @param fileName
     */
    public static void clearFile(String fileName) {
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter.write("");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(InputStream inputStream, OutputStream outputStream, Reader reader, Writer writer) {
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (outputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
