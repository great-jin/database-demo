package xyz.ibudai;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FTPTest {

    private FTPClient ftpClient;

    private final String IP = "10.231.6.65";
    private final int PORT = 21;
    private final String USERNAME = "budai";
    private final String PASSWORD = "123456";

    @Before
    public void init() throws IOException {
        ftpClient = new FTPClient();
        // Connect
        ftpClient.connect(IP, PORT);
        ftpClient.setDataTimeout(120000);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            // failed than disconnect
            ftpClient.disconnect();
        }

        // Login to ftp
        if (ftpClient.login(USERNAME, PASSWORD)) {
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // Passive connection mode
            ftpClient.enterLocalPassiveMode();
            // Encoding
            ftpClient.setControlEncoding("GBK");
            // Language code
            FTPClientConfig clientConfig = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            clientConfig.setServerLanguageCode("zh");
            ftpClient.configure(clientConfig);
        } else {
            System.out.println("Login to server failed");
        }
    }

    @After
    public void destroy() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    @Test
    public void visitDemo() throws IOException {
        boolean isCreate = ftpClient.makeDirectory("/file/test");
        System.out.println("Create success? " + isCreate);

        boolean isExist = ftpClient.changeWorkingDirectory("/file/test");
        System.out.println("Path { /file/test } is exist? " + isExist);

        ftpClient.changeToParentDirectory();
        System.out.println("Change to parent, current path: " + ftpClient.printWorkingDirectory());

        boolean isDeleted = ftpClient.removeDirectory("/file/test");
        System.out.println("Directory { /file/test } is deleted? " + isDeleted);
    }

    @Test
    public void listFile() throws IOException {
        FTPFile[] files = ftpClient.listFiles("/file");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (FTPFile file : files) {
            String updateDate = sdf.format(file.getTimestamp().getTime());
            System.out.println(file.getName() + "_" + updateDate);
        }
    }

    /**
     * Move file store path
     */
    @Test
    public void moveFile() {
        // Manual move
        try (InputStream in = ftpClient.retrieveFileStream("/test/11.txt")) {
            ftpClient.storeFile("/bak/11.txt", in);
            ftpClient.deleteFile("/test/11.txt");

            // Function equal to linux "mv"
            ftpClient.rename("/test/11.txt", "/bak/11.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete pass over 24 hour file
     */
    @Test
    public void deleteOldFile() throws IOException {
        Date currentDate = new Date(System.currentTimeMillis());
        List<FTPFile> latestList = Arrays.stream(ftpClient.listFiles("/bak"))
                .filter(it -> (currentDate.getTime() - it.getTimestamp().getTime().getTime()) / (60 * 60 * 1000) > 24)
                .collect(Collectors.toList());
        latestList.forEach(it -> {
            try {
                ftpClient.deleteFile(it.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
