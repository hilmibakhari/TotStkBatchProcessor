package net.hb.totstkbatchprocessor.record;

import net.hb.totstkbatchprocessor.common.Constant;
import org.springframework.batch.item.file.LineMapper;

/**
 * created: 20240926
 * author : hilmi
 */
public class InputLineMapper implements LineMapper<Object> {

    @Override
    public Object mapLine(String line, int lineNumber) throws Exception {
        String recordType = line.substring(0, 1); // First character defines record type

        return switch (recordType) {
            case Constant.TYPE_HEADER ->
                    mapHeader(line);
            case Constant.TYPE_BODY -> // Body
                    mapBody(line);
            case Constant.TYPE_TRAILER -> // Trailer
                    mapTrailer(line);
            default -> throw new IllegalArgumentException("Invalid record type: " + recordType);
        };
    }

    private InputHeader mapHeader(String line) {
        InputHeader header = new InputHeader();
        header.setRecType(line.substring(0, 1));
        header.setSubmissionDate(line.substring(1, 9));
        header.setSubmissionTime(line.substring(9, 15));
        header.setEtfCode(line.substring(15, 21));
        header.setEtfBusinessDate(line.substring(21, 29));
        header.setTotalStock(line.substring(29, 33));
        header.setEtfUnit(line.substring(33, 45));
        header.setComponentValue(line.substring(45, 59));
        header.setIopvValue(line.substring(59, 65));
        header.setFiller(line.substring(65,116));
        header.setHeaderChecksum(line.substring(116, 122));
        return header;
    }

    private InputBody mapBody(String line) {
        InputBody body = new InputBody();
        body.setRecType(line.substring(0, 1));
        body.setSecCode(line.substring(1, 7));
        body.setQuantity(line.substring(7, 19));
        body.setFiller(line.substring(19, 116));
        body.setBodyChecksum(line.substring(116, 122));
        return body;
    }

    private InputTrailer mapTrailer(String line) {
        InputTrailer trailer = new InputTrailer();
        trailer.setRecType(line.substring(0, 1));
        trailer.setTrailerIndicator(line.substring(1, 4));
        trailer.setFiller(line.substring(4, 116));
        trailer.setTrailerChecksum(line.substring(116, 122));
        return trailer;
    }
}
