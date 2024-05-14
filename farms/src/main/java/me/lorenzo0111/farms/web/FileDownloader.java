/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDownloader {
    private final URL url;
    private Logger logger;

    public FileDownloader(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public FileDownloader(URL url) {
        this.url = url;
    }

    public void appendLogger(Logger logger) {
        this.logger = logger;
    }

    public boolean download(File directory, @NotNull String name, boolean overwrite) {
        File file = new File(directory, name);

        if (file.exists() && (!overwrite || !file.delete())) {
            log(Level.INFO, "File already exists: " + file.getAbsolutePath(), null);
            return false;
        }

        try {
            ReadableByteChannel channel = Channels.newChannel(url.openStream());
            FileOutputStream output = new FileOutputStream(file);
            FileChannel fileChannel = output.getChannel();

            fileChannel.transferFrom(channel, 0, Long.MAX_VALUE);
            return true;
        } catch (Exception e) {
            this.log(Level.WARNING, "Unable to download the drops file", e);
            return false;
        }
    }

    private void log(Level level, String message, @Nullable Exception exception) {
        if (logger != null) {
            if (exception == null) {
                logger.log(level, message);
            } else {
                logger.log(level, message, exception);
            }
        }
    }
}
