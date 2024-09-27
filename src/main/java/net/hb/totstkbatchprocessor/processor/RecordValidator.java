package net.hb.totstkbatchprocessor.processor;

import lombok.extern.slf4j.Slf4j;
import net.hb.totstkbatchprocessor.common.Constant;

/**
 * created: 20240927
 * author : hilmi
 */
@Slf4j
public class RecordValidator {

    public static boolean isRecordInvalid(String content, String presentChecksum) {
        if (content.getBytes().length != Constant.RECORD_LENGTH_BYTE) {
            log.error("Invalid record length: present={}, expected={}",
                    content.getBytes().length, Constant.RECORD_LENGTH_BYTE);
            return true;
        }

        String calculatedChecksum = calculateChecksum(content, presentChecksum);
        boolean isValid = calculatedChecksum.equals(presentChecksum);

        if (!isValid) {
            log.error("Invalid header checksum: present={}, calculated={}",
                    presentChecksum, calculatedChecksum);
            return true;
        }

        return false;
    }

    /**
     * Calculate checksum for the provided content and return zero-padded
     * 6 characters checksum. Control characters and checksum will be striped out of the
     * content before checksum calculation
     *
     * @param content
     * @return
     */
    static String calculateChecksum(String content, String presentChecksum) {
        int checksum = 0;

        String sanitizedContent = content.replaceAll("\r|\n|" + presentChecksum, "");

        // Sum the ASCII values of each character in the content string
        for (char ch : sanitizedContent.toCharArray()) {
            checksum += ch;
        }

        // Convert the checksum to a 6-character string, padded with leading zeros if necessary
        String checksumString = String.format("%06d", checksum);

        if (checksumString.length() > 6) {
            log.warn("Checksum value is longer than 6 characters");
            checksumString = checksumString.substring(0, 6);
        }

        return checksumString;
    }
}
