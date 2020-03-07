package com.heiyogl.generation.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 文件工具类
 * @ClassName： com.heiyogl.generation.util.FileUtil.java
 * @Author： heiyogl
 */
public class FileUtil {

    public static boolean writeFile(String filePath, String content){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        }
        return false;
    }
    
    /**
     * 在指定文件中追加内容
     * @param filePath
     * @param content
     */
    public static synchronized void writeFileAppend(String filePath, String content){
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(filePath, true);
            writer.write(content);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
    
    /**
     * 读取文件内容
     * @param filePath  文件路径，如：D:/aa.txt
     * @return
     */
    public static List<String> readFileToList(String filePath) throws IOException{
        File file=new File(filePath);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        
        BufferedReader br=new BufferedReader(new FileReader(file));
        String temp=null;
        List<String> listStr = new ArrayList<String>();
        temp=br.readLine();
        while(temp!=null){
            listStr.add(temp);
            temp=br.readLine();
        }
        if (null != br) {
            br.close();
        }
        return listStr;
    }
    
    
    
    /**
     * 读取某个文件夹下面的文件名
     * @param folder 文件夹路径，如 D:/
     * @return  返回按照文件夹最后修改时间倒序排列
     */
    public static String[] readFolder(String folder){
        /***
        File file = new File(folder);
        String[] fileList = file.list();
        return fileList;
        */
        File file = new File(folder);
        File[] fileList = file.listFiles();
        bubbleSortFiles(fileList);
        String str[] = new String[fileList.length];
        for (int i = 0; i < fileList.length; i++) {
            str[i] = fileList[i].getName();
        }
        return str;
        
    }
    
    /**
     * 读取某个文件夹下面的文件名
     * @param folder 文件夹路径，如 D:/
     */
    public static String[] readFolderAsc(String folder){
        /***
        File file = new File(folder);
        String[] fileList = file.list();
        return fileList;
        */
        File file = new File(folder);
        File[] fileList = file.listFiles();
        List<Integer> fileNames = new ArrayList<Integer>();
        for(int i=0;i<fileList.length;i++) {
            String name = fileList[i].getName();
            if(isNum(name)) {
                fileNames.add(Integer.parseInt(name));
            }
        }
        Collections.sort(fileNames);
        String[] str = new String[fileNames.size()];
        for(int i=0;i<str.length;i++) {
            str[i] = fileNames.get(i).toString();
        }
        return str;
        
    }
    
    /**
     * 根据后缀读取文件
     * @param folder 文件夹路径
     * @param suffix 文件后缀名，例如：txt、log
     * @return
     */
    public static String[] readFolderWithSuffix(String folder,String suffix) {
        File file = new File(folder);
        File[] fileList = file.listFiles();
        List<Integer> fileNames = new ArrayList<Integer>();
        for(int i=0;i<fileList.length;i++) {
            String name = fileList[i].getName();
            if(name.lastIndexOf(".") != -1 ) {
                String fileName = name.substring(0,name.lastIndexOf("."));
                String fileSuffix = name.substring(name.lastIndexOf(".") + 1);
                if(isNum(fileName) && fileSuffix.equalsIgnoreCase(suffix)) {
                    fileNames.add(Integer.parseInt(fileName));
                }else {
                    continue;
                }
            }else {
                continue;
            }
        }
        Collections.sort(fileNames);
        String[] str = new String[fileNames.size()];
        for(int i=0;i<str.length;i++) {
            str[i] = fileNames.get(i).toString() + "." + suffix;
        }
        return str;
    }
    
    /**
     * 读取某个文件夹下面的所有文件名，含递归
     * @param path 文件根目录
     * @param list 获取到的所有文件名集合
     * @param limit 返回list 条数，若 limit = 0，则返回所有.
     */
    public static void readFolderAllFile(String path, List<String> list, int limit){
        String folders[] = readFolder(path);
        for (int i = 0; i < folders.length; i++) {
            if (new File(path + "/" + folders[i]).isDirectory()) {
                readFolderAllFile(path + "/" + folders[i], list, limit);
            }else{
                list.add(path + "/" + folders[i]);

                if (limit >0 && list.size() >= limit) {
                    return;
                }
            }
        }
    }
    
    
    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     * 
     * @param numbers   需要排序的Long型数组
     */
    public static void bubbleSort(long[] numbers){
        long temp;//记录临时中间值
        int size = numbers.length;//数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if(numbers[i] < numbers[j]){//交换两数的位置
                    temp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = temp;
                }
            }
        }
    }
    
    /**
     * 冒泡排序（改版）
     */
    public static void bubbleSortFiles(File[] fileList){
    	if (null == fileList) {
			return ;
		}
        File temp;//记录临时中间值
        int size = fileList.length;//数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if(fileList[i].lastModified() < fileList[j].lastModified()){//交换两数的位置
                    temp = fileList[i];
                    fileList[i] = fileList[j];
                    fileList[j] = temp;
                }
            }
        }
    }
    
    
    /**
     * 删除指定文件
     * @param filePath 文件路径
     * @return
     */
    public static boolean delFile(String filePath){
        try {
            File file = new File(filePath);
            if(file.exists()){
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 文件复制
     * @param oldPath
     * @param newPath
     */
    public static boolean copy(String oldPath, String newPath) {
        boolean result = false;
        try {
            int byteread = 0;
            File newfile = new File(newPath.substring(0, newPath.lastIndexOf("\\")));
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
                
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 得到磁盘的总空间大小，剩余空间大小，已用空间大小
     * 2015-3-19 上午08:19:41
     * @param disk 指定磁盘，如 C:
     * @param unit 指定返回单位，如：G、MB
     * @return 总空间大小、剩余空间大小、已用空间大小。若磁盘不存在，返回null；
     * 调用方式 getFreeDiskSpace("C:", "G")
     */
    public static long[] getFreeDiskSpace(String disk, String unit){
        File file = new File(disk);
        if(!file.exists()){
            return null;
        }
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        
        if (null != unit) {
            if("G".equals(unit)){
                totalSpace = totalSpace / 1024 / 1024 / 1024;
                freeSpace = freeSpace / 1024 / 1024 / 1024;
                usedSpace = usedSpace / 1024 / 1024 / 1024;
            }else if ("MB".equals(unit)) {
                totalSpace = totalSpace / 1024 / 1024;
                freeSpace = freeSpace / 1024 / 1024;
                usedSpace = usedSpace / 1024 / 1024;
            }
        }
        
        return new long[]{totalSpace, freeSpace, usedSpace};
    }
    
    /**
     * 判断文件夹是否符合规范
     * @param str
     * @return
     */
    private static boolean isNum(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
       return true;
    }     
    
    /**
     * 读取zip包里的xml内容
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "all" })
    public static HashMap<String, String> zipFileRead(String file, String charset) throws IOException {
        HashMap<String, String> resultMap = new HashMap();
        ZipFile zipFile = new ZipFile(file);
        Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile.entries();
        while (enu.hasMoreElements()) {
            ZipEntry zipElement = (ZipEntry) enu.nextElement();
            String fileName = zipElement.getName();
            InputStream inputStream = zipFile.getInputStream(zipElement);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String line = null;
            StringBuffer sbStr = new StringBuffer(256);
            boolean enterflag = false;//是否添加换行符标识
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("<TX>") != -1) {
                    enterflag = true;
                }
                if (line.indexOf("</TX>") != -1) {
                    enterflag = false;
                }
                
                //如果需要保留换行符，加入标记字段
                if (enterflag) {
                    line = line + "\\n";
                }
                
                sbStr.append(line);
            }
            inputStream.close();
            resultMap.put(fileName, sbStr.toString());
        }
        return resultMap;
    }
    
    /**
     * 递归找出文件路径
     * @param folderPath   文件夹目录
     * @return
     */
    public static List<String> ergodicGetFilePath(String folderPath) {
    	List<String> result = new ArrayList<String>();
    	
    	String[] folder = readFolder(folderPath);
    	for (String str : folder) {
			if (new File(folderPath + "\\" + str).isDirectory()) { // 文件夹
				String[] folderItem = FileUtil.readFolder(folderPath + "\\" + str);
				for (String item : folderItem) {
					if (new File(folderPath + "\\" + str + "\\" + item).isDirectory()) { // 文件夹
						result.addAll(ergodicGetFilePath(folderPath + "\\" + str + "\\" + item));
					} else {
						result.add(folderPath + "\\" + str + "\\" + item);
					}
				}
			} else {
				result.add(folderPath + "\\" + str);
			}
		}
    	return result;
    }
}
