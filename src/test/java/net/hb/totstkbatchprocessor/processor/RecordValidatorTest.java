package net.hb.totstkbatchprocessor.processor;

import net.hb.totstkbatchprocessor.common.Constant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * created: 20240927
 * author : hilmi
 */
class RecordValidatorTest {

    @Test
    void calculateChecksum_withValidRecord_returnExpectedChecksum() {
        String presentChecksum = "004010";
        String inputRecord = "11818  000000004300                                                                                                 "
                + presentChecksum
                + Constant.CTRL_CHARS;
        String actualChecksum = RecordValidator.calculateChecksum(inputRecord, presentChecksum);

        assertEquals(presentChecksum, actualChecksum);
    }
}