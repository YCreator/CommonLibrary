package com.frame.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * 文件相关工具类
 * Created by yzd on 2016/7/8.
 */
public class FileUtils {

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

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists()) { //文件不存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static String txtReader(File txtFile) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(txtFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder str = new StringBuilder();
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
                str.append(mimeTypeLine);
            }
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void txtWriter(File file, String txt) {
        try {
            FileOutputStream writerStream = new FileOutputStream(file);
            BufferedWriter oWriter = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            oWriter.write(txt);
            oWriter.flush();
            writerStream.close();
            oWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("resource")
    public static Long getFileSizes(File f) throws Exception {
        Long size = (long)0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            size = (long)fis.available();
        } else {
            f.createNewFile();
        }
        return size;
    }

    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File[] listFile = f.listFiles();
        for (File file : listFile) {
            if (file.isDirectory()) {
                size = size + getFileSize(file);
            } else {
                size = size + file.length();
            }
        }
        return size;
    }

    public static String formetFileSize(Long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String filesizeString = "";
        if (size < 1024) {
            filesizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            filesizeString = df.format((double) size/1024) + "K";
        } else if (size < 1073741824) {
            filesizeString = df.format((double) size/1048576) + "M";
        } else {
            filesizeString = df.format((double) size/1073741824) + "G";
        }
        return filesizeString;
    }

    public static String formetCacheFileSize(Long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String filesizeString = "";
        if (size < 10240) {
            filesizeString = "0.00M";
        } else if (size < 1048576) {
            filesizeString ="0"+df.format((double) size/1048576) + "M";
        } else if (size < 1073741824) {
            filesizeString = df.format((double) size/1048576) + "M";
        } else {
            filesizeString = df.format((double) size/1073741824) + "G";
        }
        return filesizeString;
    }

    public static String formetCacheFileSize(File file) throws Exception{
        Long size = getFileSize(file);
        DecimalFormat df = new DecimalFormat("#.00");
        String filesizeString = "";
        if (size < 10240) {
            filesizeString = "0.00M";
        } else if (size < 1048576) {
            filesizeString ="0"+df.format((double) size/1048576) + "M";
        } else if (size < 1073741824) {
            filesizeString = df.format((double) size/1048576) + "M";
        } else {
            filesizeString = df.format((double) size/1073741824) + "G";
        }
        return filesizeString;
    }

}
