package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.mapper.WordCountMapper;
import xyz.ibudai.reducer.WordCountReducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;

import java.io.IOException;

public class WordCountTest {

    private static final String HDFS_URL = "http://192.168.173.43:9870";
    private static final String HADOOP_HOME = "E:\\Workspace\\Hadoop\\hadoop-2.7.3";
    private static final String HDFS_HOME_DIR = "E:\\Workspace\\Hadoop\\hadoop-2.7.3";
    private static final String HADOOP_USER_NAME = "root";

    private static final String INPUT_DIRECTORY = "/test/frequency/work.txt";
    private static final String OUTPUT_DIRECTORY = "/test/frequency/output";

    @Test
    public void counter() throws IOException {
        // 需要指明 Hadoop 环境属性
        System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);
        System.setProperty("HADOOP_HOME", HADOOP_HOME);
        System.setProperty("hadoop.home.dir", HDFS_HOME_DIR);

        Configuration configuration = new Configuration();
        // 指明 HDFS 的地址
        configuration.set("fs.defaultFS", HDFS_URL);

        // 创建一个 Job
        Job job = Job.getInstance(configuration);
        // 设置运行的主类
        job.setJarByClass(WordCountTest.class);
        // 设置 Mapper 和 Reducer
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        // 设置 Mapper 输出 key 和 value 的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        // 设置 Reducer 输出 key 和 value 的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        try (FileSystem fileSystem = FileSystem.get(new URI(HDFS_URL), configuration, HADOOP_USER_NAME)) {
            // 如果输出目录已经存在，则必须先删除，否则重复运行程序时会抛出异常
            Path outputPath = new Path(OUTPUT_DIRECTORY);
            fileSystem.deleteOnExit(outputPath);

            // 设置作业输入文件和输出文件的路径
            FileInputFormat.setInputPaths(job, new Path(INPUT_DIRECTORY));
            FileOutputFormat.setOutputPath(job, outputPath);
            // 将作业提交到群集并等待它完成，参数设置为 true 代表打印显示对应的进度
            boolean result = job.waitForCompletion(true);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
