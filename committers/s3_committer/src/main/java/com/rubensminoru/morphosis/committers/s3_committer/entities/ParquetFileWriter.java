package com.rubensminoru.morphosis.committers.s3_committer.entities;

import java.io.IOException;
import java.util.List;

import com.rubensminoru.morphosis.shared.entities.RecordMessage;
import com.rubensminoru.morphosis.shared.entities.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;


public class ParquetFileWriter implements FileWriter {
    private final String localPath;
    private ParquetWriter<RecordMessage> writer;
    private final int pageSize;
    private final int blockSize;

    public ParquetFileWriter(String localPath) {
        this.localPath = localPath;
        this.pageSize = 64 * 1024;
        this.blockSize = 256 * 1024 * 1024;
    }

    public void write(List<RecordMessage> records) {
        try {
            if (!records.isEmpty()) {
                if (writer == null) {
                    this.writer = createFile(records.getFirst().getSchema());
                }
            }

            try {
                for (RecordMessage record : records) {
                    writer.write(record);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ParquetWriter<RecordMessage> createFile(Schema schema) throws IOException {
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
