package main.java.scheduler;

import main.java.constant.ProjectConstant;
import main.java.inter.ChangeWallPaper;
import main.java.utils.FileUtils;
import main.java.utils.PropertyUtil;
import main.java.utils.StringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Lei on 2019/10/19.
 */
public class GetPicJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String url = UrlHandle.getTruthUrl();
        String filePath = PropertyUtil.getProperty("file_save_path");
        String fileName = StringUtil.getFileName(url);
        try {
            FileUtils.getPict(url, "", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChangeWallPaper.change(filePath + "/" + fileName);
    }
}
