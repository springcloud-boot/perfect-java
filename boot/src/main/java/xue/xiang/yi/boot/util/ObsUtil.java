package xue.xiang.yi.boot.util;

import com.alibaba.fastjson.JSON;
import com.obs.services.ObsClient;
import com.obs.services.model.*;

import java.io.*;
import java.util.List;

public class ObsUtil {
    public static final String ENDPOINT = "obs.cn-southwest-2.myhuaweicloud.com";
    public static final String AK = "ZHQPTIVLL7HXDED5AF4P";
    public static final String SK = "04K37JRbhkjfbFUPspp0ghHoalbNCU1xGbdS9S7q";
    public static final String BUCKETNAME = "congas1";

    public static void main(String[] args) throws IOException {
// 创建ObsClient实例
        ObsClient obsClient = new ObsClient(AK, SK, ENDPOINT);

        if (!obsClient.headBucket("congas")) {
            System.out.println("不存在桶,无法上传");
            return;
        }


        ObsObject object = obsClient.getObject(BUCKETNAME, "2873deae943a4d3c939604c76b331289.xlsx");
        DownloadFileRequest req = new DownloadFileRequest(BUCKETNAME, "2873deae943a4d3c939604c76b331289.xlsx");
        DownloadFileResult downloadFileResult = obsClient.downloadFile(req);
        InputStream objectContent = object.getObjectContent();
// 关闭obsClient，全局使用一个ObsClient客户端的情况下，不建议主动关闭ObsClient客户端
        System.out.println(111);
        for (int i = 0; i < 10; i++) {
            continue;

        }
        File file = new File("2873deae943a4d3c939604c76b331289.xlsx");
        file.createNewFile();
        System.out.println(file.getAbsoluteFile());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        InputStream in = null;
        try {
            in = object.getObjectContent();
            byte[] buffer = new byte[1024];
            boolean var8 = false;

            int len;
            while((len = in.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }
        }  catch (Exception e) {
            System.out.println(e);
            throw  e;
        }finally {
            fileOutputStream.flush();
            fileOutputStream.close();
            in.close();
        }

    }


}
