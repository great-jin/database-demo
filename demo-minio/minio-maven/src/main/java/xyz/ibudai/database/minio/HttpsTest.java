package xyz.ibudai.database.minio;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.junit.Before;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsTest {

    public MinioClient minioClient;

    private static OkHttpClient okHttpClient;

    @Before
    private void init() throws KeyManagementException {
        minioClient = MinioClient.builder()
                // 填入 Minio API
                .endpoint("http://10.231.6.61:9000")
                // 填入用户名、密码
                .credentials("minioadmin", "minio123456")
                .httpClient(getUnsafeOkHttpClient())
                .build();
    }

    private OkHttpClient getUnsafeOkHttpClient() throws KeyManagementException {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            X509TrustManager x509TrustManager = (X509TrustManager) trustAllCerts[0];
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder;
            if (okHttpClient != null) {
                builder = okHttpClient.newBuilder();
            } else {
                builder = new OkHttpClient.Builder();
            }
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            builder.hostnameVerifier((s, sslSession) -> true);
            okHttpClient = builder.build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return okHttpClient;
    }
}
