package xyz.ibudai;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class MyTest {

    private final String host = "192.168.175.30";
    private final int port = 9200;
    private final boolean isAuth = true;
    private final String username = "elastic";
    private final String password = "elastic";

    private RestHighLevelClient client;

    @Before
    public void init() {
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
                    if (isAuth) {
                        // 用户认证
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(username, password));
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    } else {
                        return httpClientBuilder;
                    }
                });
        client = new RestHighLevelClient(builder);
    }

    @After
    public void destroy() throws IOException {
        client.close();
    }

    @Test
    public void testConnect() throws IOException {
        boolean ping = client.ping(RequestOptions.DEFAULT);
        System.out.println(ping);
    }

    @Test
    public void versionDemo() throws IOException {
        Version version = client.info(RequestOptions.DEFAULT).getVersion();
        System.out.println("ES version: " + version);
        System.out.println("ES lucene version: " + version.luceneVersion);
    }

    @Test
    public void listIndex() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                System.out.println(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
