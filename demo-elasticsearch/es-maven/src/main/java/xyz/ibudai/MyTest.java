package xyz.ibudai;

import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.util.ESUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MyTest {

    private RestHighLevelClient client;

    @Before
    public void init() {
        Properties props = new Properties();
        props.put("host", "127.0.0.1");
        props.put("port", 9200);
        props.put("isAuth", true);
        props.put("username", "elastic");
        props.put("password", "elastic");
        client = ESUtil.buildClient(props);
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

    @Test
    @SuppressWarnings("unchecked")
    public void listMapping() throws IOException {
        String indexName = "statement_of_account_2023-08"; // Replace with your index name
        try {
            org.elasticsearch.client.indices.GetMappingsRequest request = new GetMappingsRequest().indices(indexName);
            GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
            MappingMetaData mappingMetaData = response.mappings().get(indexName);
            Map<String, Object> mapping = mappingMetaData.sourceAsMap();
            Map<String, ?> properties = (Map<String, ?>) mapping.get("properties");
            Set<String> keySet = properties.keySet();
            for (String name : keySet) {
                String type = (String) ((Map<String, ?>) properties.get(name)).get("type");
                System.out.printf("Type: %s, \tName: %s\n", type, name);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
