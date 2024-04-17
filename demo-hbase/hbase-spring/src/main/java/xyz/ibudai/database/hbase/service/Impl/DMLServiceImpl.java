package xyz.ibudai.database.hbase.service.Impl;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.database.hbase.service.DMLService;

import java.io.IOException;
import java.util.Map;

@Service
public class DMLServiceImpl implements DMLService {

    @Autowired
    private HBaseAdmin hBaseAdmin;

    @Autowired
    private Connection connection;

    public void createTable(String name, String colFamily) throws IOException {
        TableName table = TableName.valueOf(name);
        if (!hBaseAdmin.tableExists(table)) {
            ColumnFamilyDescriptor columnDescriptor = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes(colFamily))
                    .setMaxVersions(1)
                    .build();
            TableDescriptor tableDes = TableDescriptorBuilder
                    .newBuilder(table)
                    .setColumnFamily(columnDescriptor)
                    .build();
            hBaseAdmin.createTable(tableDes);
        } else {
            System.out.println("table [" + name + "] exist.");
        }
    }

    public void putData(String name, String colFamily, String rowKey, Map<String, String> data) throws IOException {
        TableName table = TableName.valueOf(name);
        if (hBaseAdmin.tableExists(table)) {
            Table t = connection.getTable(table);
            Put put = new Put(Bytes.toBytes(rowKey));
            for (Map.Entry<String, String> entry : data.entrySet()) {
                put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            t.put(put);
            System.out.println(t);
        } else {
            System.out.println("table [" + name + "] does not exist.");
        }
    }

    public void getData(String name) throws IOException {
        TableName table = TableName.valueOf(name);
        Table t = connection.getTable(table);
        ResultScanner rs = t.getScanner(new Scan());
        for (Result r : rs) {
            System.out.println("row: " + new String(r.getRow()));
            for (Cell cell : r.rawCells()) {
                System.out.println("colFamily: " + Bytes.toString(cell.getFamilyArray()));
                System.out.println("value: " + Bytes.toString(cell.getValueArray()));
                System.out.println("qualifier: " + Bytes.toString(cell.getQualifierArray()));
                System.out.println();
            }
        }
    }

    @Override
    public void scanData(String name) throws IOException {

    }
}
