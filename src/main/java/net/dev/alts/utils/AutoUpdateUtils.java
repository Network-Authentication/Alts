package net.dev.alts.utils;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class AutoUpdateUtils {
    public static void autoUpdate(){
        ReadableByteChannel channel = null;
        File file = new File(AutoUpdateUtils.class.getResource("").getPath());
        try {
            HttpURLConnection downloadURL = (HttpURLConnection) new URL(HttpsUtils.get("https://gitee.com/azurepvp/wyalts/attach_files/441355/download")).openConnection();
            downloadURL.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Mobile Safari/537.36");
            channel = Channels.newChannel(downloadURL.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Download failed", e);
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            output.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException("File could not be saved", e);
        }
    }
}
