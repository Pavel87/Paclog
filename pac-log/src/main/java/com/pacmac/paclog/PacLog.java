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


    //filename properties
    private final long LOG_SIZE = 256 * 1024; //256kB
    private long fileSize = 0;
    private String fileName = "";
    private String fileExtension = "txt";
    private final String BCKUP_EXT = "bak";

    public final int INTERNAL_STORAGE = 0;
    public final int SECONDARY_STORAGE = 1;   // default
    public final int CUSTOM_STORAGE = 2;

    private int storageOption = SECONDARY_STORAGE;
    private String absoluthPath = "/sdcard/";
    private File logFile = null;


    private final String TAG = "PAClog";
    private Context context = null;
    private String lastMessage = null;
    private boolean isListener = false;
    private boolean isExported = false;
    private PacLogListener pacLogListener = null;


    // private constructors
    private PacLog(Context context) {
        this.context = context;
        this.fileName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        if (this.fileName.length() == 0)
            this.fileName = TAG;
        logFile = setLogPath(storageOption, fileExtension);
        initListener();
    }

    private PacLog(Context context, long fileSize, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.fileSize = fileSize < 100 ? LOG_SIZE : fileSize;
        logFile = setLogPath(storageOption, fileExtension);
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

    private File setLogPath(int storageOption, String extension) {
        if (storageOption == SECONDARY_STORAGE)
            return new File(context.getExternalFilesDir(null), fileName + "." + extension);
        else if (storageOption == INTERNAL_STORAGE)
            return new File(context.getFilesDir(), fileName + "." + extension);
        else
            return new File(absoluthPath, fileName + "." + extension);
    }


    //  Object builder
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


    private void writeLogFile(final String message) {
        // open and write file in another Thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                //prepare the log file
                if (!logFile.exists()) {
                    try {
                        logFile.createNewFile();
                        //  Log.i(TAG, "file created written");
                    } catch (IOException e) {
                        Log.e(TAG, "IOException: " + fileName + " cannot be created");
                        e.printStackTrace();
                    }
                }
                try {
                    if (logFile.length() > LOG_SIZE) {

                        File backupFile = setLogPath(storageOption, BCKUP_EXT);
                        if (backupFile.exists())
                            backupFile.delete();
                        logFile.renameTo(backupFile);
                        /// create main log file again
                        logFile = setLogPath(storageOption, fileExtension);
                        if (logFile.exists())
                            logFile.delete();
                        logFile.createNewFile();
                    }
                    //BufferedWriter for performance, true to set append to file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
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


    public void exoportInternal() {


        if (storageOption == INTERNAL_STORAGE) {

            final File src = logFile;
            final File dst = setLogPath(SECONDARY_STORAGE, fileExtension);

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
    }


    public void wipeLogs() {

        File src = logFile;
        File dst = setLogPath(SECONDARY_STORAGE, fileExtension);
        File bck = setLogPath(storageOption,BCKUP_EXT);

        if (src.exists())
            src.delete();
        if (dst.exists())
            dst.delete();
        if (bck.exists())
            bck.delete();
    }


    public void deleteLog() {

    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getAbsoluthPath() {
        return absoluthPath;
    }

    public int getStorageOption() {
        return storageOption;
    }

    public PacLog setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PacLog setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public PacLog setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public PacLog setAbsoluthPath(String absoluthPath) {
        this.absoluthPath = absoluthPath;
        return this;
    }

    public PacLog setStorageOption(int storageOption) {
        this.storageOption = storageOption;
        this.logFile = setLogPath(storageOption, fileExtension);
        return this;
    }


    public boolean isExported() {
        return isExported;
    }

    @Override
    public String toString() {
        return lastMessage;
    }


    public int getVersion() {
        PackageInfo pInfo = new PackageInfo();
        return pInfo.versionCode;
    }

}