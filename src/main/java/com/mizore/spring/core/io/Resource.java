package com.mizore.spring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface Resource {
    InputStream getInputStream() throws IOException;
}
