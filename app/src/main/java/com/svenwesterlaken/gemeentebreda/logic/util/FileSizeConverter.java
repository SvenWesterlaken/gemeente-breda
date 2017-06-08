package com.svenwesterlaken.gemeentebreda.logic.util;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Sven Westerlaken on 1-6-2017.
 */

public class FileSizeConverter {

    public static String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String fileSize(File file) {
        return readableFileSize(file.length());
    }

}
