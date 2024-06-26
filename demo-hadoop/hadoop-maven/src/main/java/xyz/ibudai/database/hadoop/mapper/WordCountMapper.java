package xyz.ibudai.database.hadoop.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // value: The hdfs file content
        String[] words = value.toString().split("\t");
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}
