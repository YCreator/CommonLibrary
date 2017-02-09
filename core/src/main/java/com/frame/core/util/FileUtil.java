package com.frame.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
