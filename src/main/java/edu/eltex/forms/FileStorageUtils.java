package edu.eltex.forms;

public class FileStorageUtils {

    public static String getUploadDir() {
        return System.getProperty("user.dir") + "/uploads/images";
    }
}

