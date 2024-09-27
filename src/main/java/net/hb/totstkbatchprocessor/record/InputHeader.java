package net.hb.totstkbatchprocessor.record;

import lombok.Data;
import net.hb.totstkbatchprocessor.common.Constant;

/**
 * created: 20240926
 * author : hilmi
 */
@Data
public class InputHeader {
    private String recType;
    private String submissionDate;
    private String submissionTime;
    private String etfCode;
    private String etfBusinessDate;
    private String totalStock;
    private String etfUnit;
    private String componentValue;
    private String iopvValue;
    private String filler;
    private String headerChecksum;

    @Override
    public String toString() {
        return recType
                + submissionDate
                + submissionTime
                + etfCode
                + etfBusinessDate
                + totalStock
                + etfUnit
                + componentValue
                + iopvValue
                + filler
                + headerChecksum
                + Constant.CTRL_CHARS;
    }
}
