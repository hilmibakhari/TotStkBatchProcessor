package net.hb.totstkbatchprocessor.record;

import lombok.Data;

/**
 * created: 20240927
 * author : hilmi
 */
@Data
public class OutputRecord {
    private String etfCode;
    private String secCode;
    private String filler1;
    private String quantity;
    private String filler2;
    private String filler3;
    private String etfUnit;
    private String component;
    private String iopv;
}
