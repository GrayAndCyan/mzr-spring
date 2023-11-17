package com.mizore.spring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileSystemResource implements Resource{

    private final File file;

    private final String path;

    public FileSystemResource(String path) {
        this.path = path;
        this.file = new File(path);
    }

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    public String getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }
}
