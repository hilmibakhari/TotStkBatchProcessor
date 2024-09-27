package net.hb.totstkbatchprocessor.processor;

import net.hb.totstkbatchprocessor.common.Constant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotStkProcessorTests {

    @Test
    void formatNumber_withTwoDecimalsAndRemoveTrailZero_returnsFormattedString() {
        String inputString = "00000000130250";
        String expectedString = "1302.5";
        String actualString = TotStkProcessor.formatNumber(inputString, Constant.TWO_DECIMAL_PATTERN);
        assertEquals(expectedString, actualString);
    }

    @Test
    void formatNumber_withThreeDecimalsAndRemoveTrailZero_returnsFormattedString() {
        String inputString = "001378";
        String expectedString = "1.378";
        String actualString = TotStkProcessor.formatNumber(inputString, Constant.THREE_DECIMAL_PATTERN);
        assertEquals(expectedString, actualString);
    }

    @Test
    void countMovePoint_withTwoDecimalPattern_returnsIntTwo() {
        assertEquals(2,TotStkProcessor.countMovePoint(Constant.TWO_DECIMAL_PATTERN) );
    }

    @Test
    void countMovePoint_withThreeDecimalPattern_returnsIntTwo() {
        assertEquals(3,TotStkProcessor.countMovePoint(Constant.THREE_DECIMAL_PATTERN) );
    }
}
