package com.projeto.biblianvi;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {

    private String packageName;
    private String TAG = "UnZip";
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private String zipFilePath;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;

    public UnZip(String zipFilePath, String packageName, SharedPreferences sharedPref, ProgressBar progressBar, FrameLayout frameLayout,
                 LinearLayout linearLayout) {
        editor = sharedPref.edit();
        this.zipFilePath = zipFilePath;
        this.packageName = packageName;
        this.progressBar = progressBar;
        this.frameLayout = frameLayout;
        this.linearLayout = linearLayout;
        runUnZip();

    }

    public UnZip(String zipFilePath, String packageName, SharedPreferences sharedPref, ProgressDialog progressDialog) {
        editor = sharedPref.edit();
        this.zipFilePath = zipFilePath;
        this.packageName = packageName;
        this.progressDialog = progressDialog;
        runUnZip();
    }

    private void runUnZip() {

        File dir = new File(getFileDestDirectory());
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(getFileDestDirectory() + File.separator + fileName);
                Log.e(TAG, "Unzipping to " + newFile.getAbsolutePath());

                //create directories for sub directories in zip
                new File(Objects.requireNonNull(newFile.getParent())).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len, lenCurrent = 0;
                long totalSize = ze.getSize();
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                    lenCurrent += len;

                    try {
                        ensureZipPathSafety(newFile, newFile.getParent());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        progressBar.setProgress((lenCurrent * 100) / (int) totalSize, true);
                        if (progressBar.getProgress() == progressBar.getMax()) {
                            progressBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    frameLayout.removeView(linearLayout);
                                }
                            });

                        }
                    } else {
                        progressDialog.setProgress((lenCurrent * 100) / (int) totalSize);
                        if (progressDialog.getProgress() == progressDialog.getMax())
                            progressDialog.dismiss();
                    }

                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
                editor.putString("dataBasePatch", newFile.getAbsolutePath());
                editor.commit();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            File fileZip = new File(zipFilePath);
            if (fileZip.exists()) {
                fileZip.delete();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ensureZipPathSafety(final File outputFile, final String destDirectory) throws Exception {
        String destDirCanonicalPath = (new File(destDirectory)).getCanonicalPath();
        String outputFilecanonicalPath = outputFile.getCanonicalPath();
        if (!outputFilecanonicalPath.startsWith(destDirCanonicalPath)) {
            throw new Exception(String.format("Found Zip Path Traversal Vulnerability with %s", destDirCanonicalPath));
        }
    }

    private String getFileDestDirectory() {

        File folderStorage;
        String folderDest = "Android/data/" + packageName + "/databases";

        //Get File if SD card is present
        if (new DownloadTask.CheckForSDCard().isSDCardPresent()) {

            folderStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + folderDest);

            //If File is not present create directory
            if (!folderStorage.exists()) {
                folderStorage.mkdir();
            }

        } else {

            folderStorage = new File(
                    Environment.getDataDirectory() + "/"
                            + folderDest);

            //If File is not present create directory
            if (!folderStorage.exists()) {
                folderStorage.mkdir();

            }
        }

        Log.e(TAG, "Directory Created: " + folderStorage.getAbsolutePath());
        return folderStorage.getAbsolutePath();


    }
}