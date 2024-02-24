package com.rubensminoru.morphosis.committers.s3_committer.entities;

import com.rubensminoru.morphosis.consumers.shared.entities.Record;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;


public class ParquetFileWriter implements FileWriter {
    private final String localPath;
    private ParquetWriter<Record> writer;
    private final int pageSize;
    private final int blockSize;

    public ParquetFileWriter(String localPath, Schema schema) throws IOException {
        this.localPath = localPath;
        this.pageSize = 64 * 1024;
        this.blockSize = 256 * 1024 * 1024;
        this.writer = createFile(schema);
    }

    public void write(List<Record> records) {
        try {
            for (Record record : records) {
                writer.write(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void commit() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ParquetWriter<Record> createFile(Schema schema) throws IOException {
        Configuration conf = new Configuration();
        Path outputPath = new Path(localPath);

        return new ParquetWriter<>(
                outputPath,
                new RecordWriterSupport(schema),
                CompressionCodecName.SNAPPY,
                blockSize,
                pageSize,
                512,
                true,
                false,
                ParquetWriter.DEFAULT_WRITER_VERSION,
                conf
        );
    }
}
