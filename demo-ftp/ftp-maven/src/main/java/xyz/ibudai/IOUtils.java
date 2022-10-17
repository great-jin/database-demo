package xyz.ibudai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.model.User;
import xyz.ibudai.util.GZIPTest;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IOUtils {

    private FTPClient ftpClient;
    private final static ObjectMapper objectMapper = new ObjectMapper();

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

    /**
     * Upload source type is "InputStream"
     * If upload file from disk, then use "FileInputStream"
     * Otherwise can use "ByteArrayOutputStream" to upload byte data.
     */
    @Test
    public void uploadFile() {
        String remotePath = "/test/ftp-1.txt";
        String localPath = "src\\main\\resources\\ftp\\ftp-1.txt";
        try (InputStream in = new FileInputStream(localPath)) {
            // Upload file
            boolean isUpload = ftpClient.storeFile(remotePath, in);

            System.out.println(isUpload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Download source type is "OutputStream"
     * If needed save to disk, then use "FileOutputStream"
     * Otherwise can use "ByteArrayOutputStream" to save as byte.
     */
    @Test
    public void downloadFile() {
        String remotePath = "/test/ftp-1.txt";
        String localPath = "src\\main\\resources\\ftp\\ftp-1.txt";
        try (OutputStream fos = new FileOutputStream(localPath)) {
            // Download file
            boolean isSave = ftpClient.retrieveFile(remotePath, fos);

            System.out.println(isSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Package local file to FTP
     */
    @Test
    public void uploadData() throws IOException {
        String putPath = "/test/store";
        String filePath = putPath + "/" + UUID.randomUUID() + ".gz";
        // Compress content to gzip
        User user = new User("123", "Alex", "123456");
        byte[] bytes = GZIPTest.compress(objectMapper.writeValueAsBytes(user));

        // Upload file
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            if (ftpClient.storeFile(filePath, in)) {
                System.out.println("Upload file: 【" + filePath + "】 success.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadAndMonitor() throws InterruptedException {
        String directory = "/test/read";
        new Thread(() -> {
            try {
                monitorDirectory(directory);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).start();
        TimeUnit.SECONDS.sleep(2);

        new Thread(() -> {
            while (true) {
                String uuid = UUID.randomUUID().toString();
                String fileName = directory + "/" + uuid + ".gz";
                // Compress content to gzip
                User user = new User(uuid, "Alex", "123456");
                // Upload file
                try (InputStream in = new ByteArrayInputStream(GZIPTest.compress(objectMapper.writeValueAsBytes(user)))) {
                    if (ftpClient.storeFile(fileName, in)) {
                        System.out.println("\nupload: 【" + fileName + "】 success.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        TimeUnit.HOURS.sleep(1);
    }

    /**
     * Download FTP file to local
     */
    public void downloadData(String filePath) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // Receive ftp file
            ftpClient.retrieveFile(filePath, os);

            // Uncompress zip to java bean
            byte[] bytes = GZIPTest.uncompress(os.toByteArray());
            User user = objectMapper.readValue(bytes, User.class);
            System.out.println("Receive: " + user);
            ftpClient.deleteFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listening ftp directory file change
     */
    public void monitorDirectory(String monitorPath) throws Exception {
        List<String> fileName = Arrays.stream(ftpClient.listFiles(monitorPath))
                .map(FTPFile::getName)
                .collect(Collectors.toList());

        System.out.println("Start monitoring directory: " + monitorPath);
        while (true) {
            List<String> latestList = Arrays.stream(ftpClient.listFiles(monitorPath))
                    .map(FTPFile::getName)
                    .collect(Collectors.toList());
            latestList.removeAll(fileName);
            if (latestList.size() > 0) {
                System.out.println("Detect new file: " + latestList);
                latestList.forEach(name -> {
                    downloadData(monitorPath + "/" + name);
                });
            }
            TimeUnit.SECONDS.sleep(2);
        }
    }
}


