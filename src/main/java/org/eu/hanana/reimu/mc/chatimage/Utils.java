package org.eu.hanana.reimu.mc.chatimage;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static String FileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        int returnValue = fileChooser.showOpenDialog(Display.getParent()); // 打开文件选择对话框

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // 用户选择了一个文件
            java.io.File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            return null;
        }
    }
    public static boolean OK;
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
    public static byte[] ReadFile(File file)
     {


        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[(int) file.length()]; // 创建与文件大小相同的字节数组

            fis.read(byteArray); // 从文件读取字节并存储到字节数组中
            fis.close();

            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void WriteFile(String filePath,byte[] contentBytes) {

        try {
            // 创建FileOutputStream对象
            FileOutputStream fos = new FileOutputStream(filePath);

            // 使用FileOutputStream将字节数组写入文件
            fos.write(contentBytes);

            // 关闭文件输出流
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean SEND_FINISH=true;
    public static String SendLByte(byte[] bytes) throws InterruptedException {
        int maxPacketSize = 32700; // 限制单个数据包大小

        while (!SEND_FINISH) Thread.sleep(10);
        SEND_FINISH=false;

        List<byte[]> data=new ArrayList<>();
        // 分割数据并发送
        for (int i = 0; i < bytes.length; i += maxPacketSize) {
            int length = Math.min(bytes.length - i, maxPacketSize);
            byte[] chunk = Arrays.copyOfRange(bytes, i, i + length);
            data.add(chunk);
        }
        OK=false;
        ChatImageMod.INSTANCE.sendToServer(new UploadMMessage(data.size(),"start"));
        while (!OK){
            Thread.sleep(1);
        }
        for (byte[] datum : data) {
            OK=false;
            ChatImageMod.INSTANCE.sendToServer(new UploadMessage(datum, data.indexOf(datum)));
            while (!OK){
                Thread.sleep(1);
            }
        }
        ChatImageMod.INSTANCE.sendToServer(new UploadMMessage(0,"finish"));
        SEND_FINISH=true;
        if (SvReply instanceof Integer)
            return "ci:lo/"+SvReply;
        return null;
    }
    @SideOnly(Side.CLIENT)
    public static Object SvReply;
    @SideOnly(Side.CLIENT)
    public static String GetSvURL(File file) throws InterruptedException {
        String md5 = calcMD5(file);

        while (!SEND_FINISH) Thread.sleep(10);
        SEND_FINISH=false;
        OK=false;

        ChatImageMod.INSTANCE.sendToServer(new Caclmd5Message(md5));
        while (!OK){
            Thread.sleep(1);
        }
        SEND_FINISH=true;
        if (SvReply instanceof String)
            return (String) SvReply;
        return null;
    }
    @SideOnly(Side.CLIENT)
    public static byte[] GetSvDATA(int fn) throws InterruptedException {

        while (!SEND_FINISH) Thread.sleep(10);
        SEND_FINISH=false;
        OK=false;

        ChatImageMod.INSTANCE.sendToServer(new DownloadMessage(new byte[0],fn));
        while (!OK){
            Thread.sleep(1);
        }
        SEND_FINISH=true;
        if (SvReply instanceof byte[])
            return (byte[]) SvReply;
        return null;
    }
    // 文件类取MD5
    public static String calcMD5(File file){
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            return calcMD5(stream);
        }catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    // 输入流取MD5
    public static String calcMD5(InputStream stream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
    public static List<File> traverseFolder(File folder) {
        List<File> fl = new ArrayList<>();
        if (folder.isDirectory()) {

            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        traverseFolder(file); // 递归遍历子文件夹

                    } else {
                        fl.add(file);

                    }

                }

            }

        }
        return fl;
    }

}