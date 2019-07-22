package com.yinker.etl.pm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines (File file) {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while((tempString = reader.readLine()) != null) {
                // 显示行号
                LOGGER.debug("line " + line + ": " + tempString);
                sb.append(tempString);
                line++;
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch(IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete (String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile (String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory (String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for(int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加载properties文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Properties getPropertiesByFile (String path) throws IOException {
        Properties prop = new Properties();
        //读取属性文件
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        //加载属性列表
        prop.load(in);
        return prop;
    }

    /**
     * 密文写入文件
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String writeToFile (String content,String fileLocation) throws Exception {
        String filePath;
        String fileName = UUID.randomUUID().toString();
        try {
            if (!(new File(fileLocation).isDirectory())) {
                new File(fileLocation).mkdir();
            }
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        filePath = fileLocation + fileName + ".cbw";
        LOGGER.info("存储文件路径为:{}", filePath);
        contentToTxt(filePath, content);
        return filePath;
    }

    /**
     * 文字写入文档
     *
     * @param filePath
     * @param content
     */
    public static void contentToTxt (String filePath, String content) {
        LOGGER.info("写入文档的内容为:{}", content);
        FileOutputStream fop = null;
        File file;

        try {

            file = new File(filePath);
            fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            LOGGER.info("创建文件成功");
            FileWriter fw = new FileWriter(file);
            BufferedWriter buffw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(buffw);

            pw.println(content);
            pw.close();
            buffw.close();
            fw.close();

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
