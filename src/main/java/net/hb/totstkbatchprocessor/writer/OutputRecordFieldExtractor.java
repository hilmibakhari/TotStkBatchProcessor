package net.hb.totstkbatchprocessor.writer;

import net.hb.totstkbatchprocessor.record.OutputRecord;
import org.springframework.batch.item.file.transform.FieldExtractor;

/**
 * created: 20240927
 * author : hilmi
 */
public class OutputRecordFieldExtractor implements FieldExtractor<OutputRecord> {

    @Override
    public Object[] extract(OutputRecord outputRecord) {
        return new Object[] {
                outputRecord.getEtfCode(),
                outputRecord.getSecCode(),
                outputRecord.getFiller1(),
                outputRecord.getQuantity(),
                outputRecord.getFiller2(),
                outputRecord.getFiller3(),
                outputRecord.getEtfUnit(),
                outputRecord.getComponent(),
                outputRecord.getIopv()
        };
    }
}
