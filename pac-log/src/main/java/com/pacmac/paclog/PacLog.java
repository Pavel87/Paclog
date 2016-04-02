package com.pacmac.paclog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by pacmac on 24/03/16.
 */

public class PacLog {

    private static long LOG_SIZE = 256 * 1024; //256kB
    private static String TAG = "PAClog";
    private String fileName = "log.txt";
    private long fileSize = 0;
    private Context context = null;
    private String lastMessage = null;

    private boolean isListener = false;
    private boolean isExported = false;

    private PacLogListener pacLogListener = null;

    private PacLog(Context context, long fileSize, String fileName) {
        this.context = context;
        this.fileName = fileName != null ? fileName : this.fileName;
        this.fileSize = fileSize < 100 ? LOG_SIZE : fileSize;
        initListener();
    }


    private PacLog(Context context) {
        this.context = context;
        initListener();
    }

    private void initListener() {
        if (this.context instanceof PacLogListener) {
            pacLogListener = (PacLogListener) context;
            isListener = true;
        } else {
            isListener = false;
        }
    }

    public static PacLog setUP(Context context, long fileSize, String fileName) {
        PacLog pacLog = new PacLog(context, fileSize, fileName);
        return pacLog;
    }


    public static PacLog setUP(Context context) {
        PacLog pacLog = new PacLog(context);
        return pacLog;
    }


    public void writePacLog(String message) {
        this.lastMessage = new DataEntry(message).getNewLine();
        writeLogFile(this.lastMessage);
    }


    public String getVersion() {
        PackageInfo pInfo = new PackageInfo();
        return pInfo.versionName;
    }


    private void writeLogFile(final String message) {
        // open and write file in another Thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                //prepare the log file
                File file = new File(context.getFilesDir(), fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        //  Log.i(TAG, "file created written");
                    } catch (IOException e) {
                        Log.e(TAG, "IOException: " + fileName + " cannot be created");
                        e.printStackTrace();
                    }
                }
                try {
                    if (file.length() > LOG_SIZE) {
                        file.delete();
                        file.createNewFile();
                    }
                    //BufferedWriter for performance, true to set append to file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                    buf.append(message);
                    buf.newLine();
                    buf.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + fileName + " cannot be written");
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void exoportLog() {

        final File src = new File(context.getFilesDir(), fileName);
        final File dst = new File(context.getExternalFilesDir(null), fileName);

        if (src.exists()) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        InputStream is = new FileInputStream(src);
                        OutputStream os = new FileOutputStream(dst);

                        byte[] buff = new byte[1024];
                        int len;

                        while ((len = is.read(buff)) > 0) {
                            os.write(buff, 0, len);
                        }
                        is.close();
                        os.close();
                        if (isListener)
                            pacLogListener.onExport(true);
                        isExported = true;
                        Log.i(TAG, "Log exported: " + dst.getAbsolutePath());
                    } catch (IOException ex) {
                        if (isListener)
                            pacLogListener.onExport(false);
                        isExported = false;
                        Log.d(TAG, "IOException during file export");
                        ex.printStackTrace();
                    }
                }
            }).start();

        } else {
            if (isListener)
                pacLogListener.onExport(false);
            isExported = false;
            Log.d(TAG, "File does not exist!");
        }
    }


    public void deleteALl() {

        File src = new File(context.getFilesDir(), fileName);
        File dst = new File(context.getExternalFilesDir(null), fileName);

        if (src.exists())
            src.delete();
        if (dst.exists())
            dst.delete();
    }


    public void deleteExported() {

        File exported = new File(context.getExternalFilesDir(null), fileName);

        if (exported.exists())
            exported.delete();
    }

    public void deleteInternal() {

        File internal = new File(context.getFilesDir(), fileName);

        if (internal.exists())
            internal.delete();
    }




    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public PacLog setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PacLog setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public boolean isExported() {
        return isExported;
    }

    @Override
    public String toString() {
        return lastMessage;
    }

//TODO add option to chose location internal/external/custom path
//TODO add option to create specific amount of files and create logic for writing these files
}