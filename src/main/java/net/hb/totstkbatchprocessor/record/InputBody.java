package net.hb.totstkbatchprocessor.record;

import lombok.Data;
import net.hb.totstkbatchprocessor.common.Constant;

/**
 * created: 20240926
 * author : hilmi
 */
@Data
public class InputBody {
    private String recType;
    private String secCode;
    private String quantity;
    private String filler;
    private String bodyChecksum;

    @Override
    public String toString() {
        return recType
                + secCode
                + quantity
                + filler
                + bodyChecksum
                + Constant.CTRL_CHARS;
    }
}
