package com.frame.core.util;

import java.io.File;

/**
 * Created by yzd on 2016/7/8.
 */
public class FileUtil {

    /**
     * 删除文件夹下的所有文件
     * @param files         文件夹
     * @return
     */
    public static boolean deleteFiles(File files) {
        if(files.isDirectory()) {
            File[] file = files.listFiles();
            for (File f : file) {
                if (!f.delete()) {
                    return false;
                }
            }
            return true;
        }
        return files.delete();
    }

}
