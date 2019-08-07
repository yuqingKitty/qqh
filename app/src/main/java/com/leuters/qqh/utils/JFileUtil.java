package com.leuters.qqh.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Locale;

/**
 * 基本使用的是JAVA IO的方法进行文件的操作
 */
public final class JFileUtil {

    /**
     * FileUtil . <br>
     */
    private JFileUtil() {
    }

    /**
     * 把字符串数据写入到指定的完整目录文件上 . <br>
     *
     * @param data   数据内容
     * @param path   完整路径
     * @param unio   编码"utf-8"之类的
     * @param append 是否追加
     * @throws IOException IO错误
     */
    public static void writeDataToFile(final String data, final String path, final String unio, final boolean append)
            throws IOException {
        OutputStreamWriter osw;
        final File file = new File(path);
        if (!append) {
            delFile(path);
        }
        if (!checkFileExists(path)) {
            file.createNewFile();
        }
        osw = new OutputStreamWriter(new FileOutputStream(file, append), unio);
        osw.write(data);
        osw.close();

    }

    /**
     * 多线程读写的时候需要注意，外部的方法需要同步 . <br>
     *
     * @param fileFullPath 写入的文件路径
     * @param pBuf         写入文件的缓存
     * @return 如果文件及路径不存在将创建文件
     */
    public static boolean write(final String fileFullPath, final byte[] pBuf) {
        return write(fileFullPath, pBuf, 0, pBuf.length, 0);
    }

    /**
     * 多线程读写的时候需要注意，外部的方法需要同步. <br>
     *
     * @param fileFullPath 写入的文件路径
     * @param pBuf         写入文件的缓存
     * @param pos          缓存起始位置
     * @param size         写入字节数
     * @param skip         文件跳跃字节数
     * @return 如果文件及路径不存在将创建文件
     */
    public static boolean write(final String fileFullPath, final byte[] pBuf, final int pos, final int size,
                                final int skip) {
        final File file = new File(fileFullPath);
        RandomAccessFile randomAccessFile = null;
        try {
            // 文件存在
            if (file.exists()) {
                randomAccessFile = new RandomAccessFile(file, "rw");
            } else { // 文件不存在

                // 判断父目录是否存在
                final File p = file.getParentFile();
                boolean isOk = false;
                if (p != null && !p.exists()) { // 目录不存在创建

                    isOk = p.mkdirs();
                } else if (p != null && p.exists()) { // 目录存在

                    isOk = true;
                }
                if (isOk) {
                    final boolean isSucess = file.createNewFile();
                    if (isSucess) {
                        randomAccessFile = new RandomAccessFile(file, "rw");
                    }
                }
            }
            if (null != randomAccessFile) {
                randomAccessFile.seek(skip);
                randomAccessFile.write(pBuf, pos, size);
                randomAccessFile.close();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 一行一行的读取,注意如果文本较大的时候要小心使用. <br>
     *
     * @param path 完整路径
     * @param unio 编码
     * @return 读取的字符串
     * @throws IOException IO错误
     */
    public static String readFileData(final String path, final String unio) throws IOException {
        final InputStreamReader isr = new InputStreamReader(new FileInputStream(path), unio);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder str = new StringBuilder();
        while (br.ready()) {
            str.append(br.readLine());
        }
        br.close();
        isr.close();
        return str.toString();

    }

    /**
     * 以流的方式,读取一个文件的字节 . <br>
     *
     * @param fileFullPath 读取文件路径
     * @return 返回整个文件数据 流
     */
    public static byte[] readFilebyte(final String fileFullPath) {
        final File file = getFileObject(fileFullPath);
        if (file != null && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                final int readSize = fis.available();
                final byte[] buff = new byte[readSize];
                fis.read(buff);
                fis.close();
                fis = null;
                return buff;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 删除文件 . <br>
     *
     * @param path 完整路径
     * @return 是否删除成功
     */
    public static boolean delFile(final String path) {
        if (path != null && !"".equals(path)) {
            final File f = new File(path);
            if (f.isFile()) {
                return f.delete();
            }
        }
        return false;
    }

    /**
     * 判断指定文件是否存在 . <br>
     *
     * @param filePath 文件路径
     * @return 文件是否存在
     */
    public static Boolean checkFileExists(final String filePath) {
        File f = getFileObject(filePath);
        try {
            return f != null && f.exists() && f.isFile();
        } finally {
            f = null;
        }
    }

    /**
     * 返回文件对像 . <br>
     *
     * @param filePath 要创建文件的文件路径
     * @return File 文件对像
     */
    public static File getFileObject(final String filePath) {
        if (filePath == null || filePath.length() <= 0) {
            return null;
        }
        return new File(filePath);
    }


    /**
     * 通过uri获取路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                switch (type) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return "";
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {MediaStore.MediaColumns.DATA};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 将字节流转换成文件
     *
     * @param filepath 文件路径
     * @param data     文件字节
     * @throws Exception
     */
    public synchronized static void saveFile(String filepath, byte[] data) throws Exception {
        if (data != null) {
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.flush();
            fos.close();
        }
    }

    /**
     * 保存bitmap到file
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @param bitmap
     * @param f
     */
    public synchronized static String saveBitmap(Bitmap bitmap, File f) throws IOException {
        if (!f.isDirectory() && f.exists()) {
            f.delete();
        }

        if (!f.getParentFile().exists()) {
            f.mkdirs();
        }

        try (FileOutputStream out = new FileOutputStream(f)) {
            Bitmap.CompressFormat fmt;
            if (f.getPath().toLowerCase(Locale.US).endsWith(".png")) {
                fmt = Bitmap.CompressFormat.PNG;
            } else {
                fmt = Bitmap.CompressFormat.JPEG;
            }
            bitmap.compress(fmt, 90, out);
            out.flush();
        } catch (IOException e) {
            throw e;
        }
        return f.getAbsolutePath();
    }

    /**
     * 图片旋转
     *
     * @param bit     旋转原图像
     * @param degrees 旋转度数
     * @return 旋转之后的图像
     */
    public static Bitmap rotateImage(Bitmap bit, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                bit.getHeight(), matrix, true);
        return tempBitmap;
    }

    public static String extractFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
