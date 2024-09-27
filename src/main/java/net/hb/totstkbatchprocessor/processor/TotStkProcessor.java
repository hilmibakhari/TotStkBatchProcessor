package net.hb.totstkbatchprocessor.processor;

import lombok.extern.slf4j.Slf4j;
import net.hb.totstkbatchprocessor.common.Constant;
import net.hb.totstkbatchprocessor.exception.InvalidHeaderException;
import net.hb.totstkbatchprocessor.record.InputBody;
import net.hb.totstkbatchprocessor.record.InputHeader;
import net.hb.totstkbatchprocessor.record.InputTrailer;
import net.hb.totstkbatchprocessor.record.OutputRecord;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * created: 20240927
 * author : hilmi
 */
@Slf4j
public class TotStkProcessor implements ItemProcessor<Object, OutputRecord> {
    private InputHeader currentHeader;


    @Override
    public OutputRecord process(Object item) throws Exception {
        if (item instanceof InputHeader header) {
            if (RecordValidator.isRecordInvalid(header.toString(), header.getHeaderChecksum())) {
                log.error("Header record violated record validation");
                //TODO: inject filename which has violation
                throw new InvalidHeaderException("Invalid header detected. Skipping file processing");
            }

            currentHeader = header;

            return null;

        } else if (item instanceof InputBody body && currentHeader != null) {

            if (RecordValidator.isRecordInvalid(body.toString(), body.getBodyChecksum())) {
                log.error("Body record violated record validation");
                return null;
            }

            return getOutputRecord(body, currentHeader);

        } else if(item instanceof InputTrailer trailer) {

            if (RecordValidator.isRecordInvalid(trailer.toString(), trailer.getTrailerChecksum())) {
                log.error("Trailer record violated record validation");
                return null;
            }

        }

        return null;
    }

    private OutputRecord getOutputRecord(InputBody body, InputHeader currentHeader) {
        OutputRecord outputRecord = new OutputRecord();
        outputRecord.setEtfCode(currentHeader.getEtfCode());
        outputRecord.setSecCode(body.getSecCode());
        outputRecord.setFiller1("NM");
        outputRecord.setQuantity(Integer.valueOf(body.getQuantity().trim()).toString());
        outputRecord.setFiller2("");
        outputRecord.setFiller3("");
        outputRecord.setEtfUnit(Integer.valueOf(currentHeader.getEtfUnit()).toString());
        outputRecord.setComponent(formatNumber(currentHeader.getComponentValue(), Constant.TWO_DECIMAL_PATTERN));
        outputRecord.setIopv(formatNumber(currentHeader.getIopvValue(), Constant.THREE_DECIMAL_PATTERN));
        return outputRecord;
    }

    public static String formatNumber(String input, String decimalPattern) {

        BigDecimal decimalValue = new BigDecimal(input).movePointLeft(countMovePoint(decimalPattern));  // Move decimal 3 places to the left for 3 decimals

        DecimalFormat decimalFormat = new DecimalFormat(decimalPattern);

        return decimalFormat.format(decimalValue);
    }

    public static int countMovePoint(String decimalPattern) {
        int decimalIndex = decimalPattern.indexOf('.');

        if (decimalIndex == -1) {
            return 0;
        }

        return decimalPattern.length() - decimalIndex - 1;
    }
}
