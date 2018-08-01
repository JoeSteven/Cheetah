package cheetah.core.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
public class FileUtil {

    public static void ensureDirExists(String path) {
        if (path == null) {
            return;
        }
        File dir = new File(path);
        ensureDirExists(dir);
    }

    public static boolean ensureDirExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.exists();
    }

    /**
     * @return sd卡是否可读
     */
    public static boolean isSdcardAvailable() {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * @return sd卡是否可写
     */
    public static boolean isSdcardWritable() {
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @return sd卡可用大小
     */
    public static long getSDAvailableSize() {
        try {
            if (!isSdcardAvailable()) return 0L;
            File sdDir = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(sdDir.getPath());
            long availableSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableSize = statFs.getAvailableBytes();
            } else {
                availableSize = ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
            }
            return availableSize;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 重命名
     *
     * @param oldPath 老名字
     * @param newPath 新名字
     * @return
     */
    public static boolean renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) return false;
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.exists() && oldFile.renameTo(newFile);
    }


    /**
     * 文件复制
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return
     */
    public static boolean copy(File srcFile, File destFile) {
        if (srcFile == null || destFile == null) return false;
        return copy(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
    }

    /**
     * 文件复制
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return
     */
    public static boolean copy(String srcFilePath, String destFilePath) {
        if (!isSdcardWritable() || TextUtils.isEmpty(srcFilePath) || TextUtils.isEmpty(destFilePath)) return false;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inputStream = new FileInputStream(srcFilePath);
            outputStream = new FileOutputStream(destFilePath);
            inChannel = inputStream.getChannel();
            outChannel = outputStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @param path 删除文件
     */
    public static boolean removeFile(String path) {
        if (!isSdcardWritable() || TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        return file.exists() && file.canWrite() && file.delete();
    }

    /**
     * 获取指定文件大小(单位：字节)
     */
    public static long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    public static long getFileSize(String path) {
        try {
            File file = new File(path);
            return getFileSize(file);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取文件夹大小
     */
    public static long getDirSize(String path) {
        if (!isSdcardWritable() || TextUtils.isEmpty(path)) return 0;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) return 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        long totalSize = 0;
        for (File file : files) {
            if (file.isFile()) {
                totalSize += file.length();
            } else if (file.isDirectory()) {
                totalSize += getDirSize(file.getAbsolutePath());
            }
        }

        return totalSize;
    }


    /**
     * 删除文件夹
     */
    public static void removeDir(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] files = fileOrDirectory.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File child : files) {
                if (child.isDirectory()) {
                    removeDir(child);
                } else {
                    child.delete();
                }
            }

        }
    }


    /**
     * 检查文件是否存在
     */
    public static boolean checkFileExists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }


    /**
     * enhance @checkFileExists
     *
     * @param path sdcard path
     * @param size file size
     * @return
     */
    public static boolean checkFileBySize(String path, long size) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        return file.exists() && !file.isDirectory() && file.length() == size;
    }

    /**
     * 创建文件
     */
    public static File createFile(String path, boolean isFile) {
        if (!TextUtils.isEmpty(path)) {
            File f = new File(path);
            if (!f.exists()) {
                if (!isFile) {
                    f.mkdirs();
                } else {
                    try {
                        File parent = f.getParentFile();
                        if (!parent.exists()) {
                            parent.mkdirs();
                        }
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return f;
        }

        return null;
    }

    /**
     * 解压zip文件
     */
    public static void unZipFolder(String zipFilePath, String outPath) throws IOException {
        ZipInputStream inZip;
        BufferedOutputStream out;
        inZip = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFilePath)));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            String szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                File folder = new File(outPath + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPath + File.separator + szName);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                // get the output stream of the file
                out = new BufferedOutputStream(new FileOutputStream(file));
                int len;
                byte[] buffer = new byte[2048];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
            }
        }//end of while
        inZip.close();
    }//end of func

    /**
     * 插入图片到相册
     */
    public static void saveImageToGallery(Context context, File file) {
        String path = file.getAbsolutePath();
        String fileName = file.getName();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

    }


    /**
     * 写文件
     */
    public synchronized static void writeToFile(String input, File file) {
        if (!file.exists()) {
            createFile(file.getPath(), true);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件
     */
    public static String readFromFile(File file) {
        if (checkFileExists(file.getPath())) {
            String result = "";
            FileReader reader = null;
            BufferedReader br = null;
            try {
                reader = new FileReader(file);
                br = new BufferedReader(reader);
                String str;
                while ((str = br.readLine()) != null) {
                    result += str;
                }
                str = null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * 计算文件md5值
     */
    public static String calculateMD5(File file) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
}
