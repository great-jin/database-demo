package xyz.ibudai.database.es.util;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Properties;

public class ESUtil {

    public static RestHighLevelClient buildClient(Properties props) {
        String host = props.getProperty("host");
        int port = (int) props.get("port");
        // 连接对象构建
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                // 连接超时配置
                .setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(3000)
                                .setSocketTimeout(5000)
                                .setConnectionRequestTimeout(500))
                // 异步连接数配置
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setMaxConnTotal(100);
                    httpClientBuilder.setMaxConnPerRoute(100);
                    boolean isAuth = (boolean) props.get("isAuth");
                    if (isAuth) {
                        // 用户认证
                        String username = props.getProperty("username");
                        String password = props.getProperty("password");
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(username, password));
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    } else {
                        return httpClientBuilder;
                    }
                });
        return new RestHighLevelClient(builder);
    }
}
