package net.hb.totstkbatchprocessor.record;

import lombok.Data;
import net.hb.totstkbatchprocessor.common.Constant;

/**
 * created: 20240926
 * author : hilmi
 */
@Data
public class InputTrailer {
    private String recType;
    private String trailerIndicator;
    private String filler;
    private String trailerChecksum;

    @Override
    public String toString() {
        return recType
                + trailerIndicator
                + filler
                + trailerChecksum
                + Constant.CTRL_CHARS;
    }
}
