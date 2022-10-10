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
        String remotePath = "/file/test.txt";
        String localPath = "src\\main\\resources\\ftp\\test.txt";
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
        String remotePath = "/file/11.txt";
        String localPath = "src\\main\\resources\\ftp\\11.txt";
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
        String putPath = "/file";
        String filePath = putPath + "/" + System.currentTimeMillis() + ".gz";
        // Compress content to gzip
        User user = new User("111", "Alex", "123456");
        byte[] bytes = GZIPTest.compress(objectMapper.writeValueAsBytes(user));

        try (InputStream in = new ByteArrayInputStream(bytes)) {
            // Upload file
            boolean isOk = ftpClient.storeFile(filePath, in);

            System.out.println(isOk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download FTP file to local
     */
    @Test
    public void downloadData() {
        String remotePath = "/file/1665221694460.gz";
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // Receive ftp file
            ftpClient.retrieveFile(remotePath, os);

            // Uncompress zip to java bean
            byte[] bytes = GZIPTest.uncompress(os.toByteArray());
            User user = objectMapper.readValue(bytes, User.class);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listening ftp directory file change
     */
    @Test
    public void monitorDirectory() throws Exception {
        List<String> fileName = Arrays.stream(ftpClient.listFiles("/test"))
                .map(FTPFile::getName)
                .collect(Collectors.toList());

        System.out.println("Start monitoring....");
        while (true) {
            List<String> latestList = Arrays.stream(ftpClient.listFiles("/test"))
                    .map(FTPFile::getName)
                    .collect(Collectors.toList());
            latestList.removeAll(fileName);
            if (latestList.size() > 0) {
                System.out.println("Detect new file: " + latestList);
                latestList.forEach(name -> {
                    try {
                        ftpClient.rename("/test/" + name, "/bak/" + name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            TimeUnit.SECONDS.sleep(2);
        }
    }
}


