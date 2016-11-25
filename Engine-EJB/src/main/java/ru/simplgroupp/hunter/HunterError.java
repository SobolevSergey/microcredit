package ru.simplgroupp.hunter;

import java.util.HashMap;
import java.util.Map;

public enum HunterError {
    UNKNOWN_ERROR(102999),
    UNKNOWN_CLIENT_IDENTIFIER(102001),
    CONTROL_XSD_VALIDATION_FAILURE(102002),
    LOAD_AND_SUPPRESS_FLAGS_SET(102003),
    LOAD_AND_SUPPRESS_FLAGS_NOT_SET(102004),
    INVALID_SUBMISSION_IDENTIFIER(102005),
    NO_MATCH_SCHEMES_PROVIDED(102006),
    PERSIST_AND_WORKLIST_INVALID_COMBINATION(102007),
    INVALID_PRODUCT_CODE(102008),
    MATCH_SCHEMES_NOT_USED(102009),
    INITIALISATION_ERROR(102010),
    ERROR_CREATING_LEFT_HAND_SUBMISSION(102011),
    UNABLE_TO_CREATE_LEFT_HAND_SUBMISSION(102012),
    MISSING_INPUT_PARAMETERS(102013),
    PASSWORD_EXPIRED(102014),
    INVALID_LOGON(102015),
    SERVICE_UNAVAILABLE(102016);

    private final static Map<Integer, HunterError> errorCodeMap = new HashMap<>();

    static {
        for (HunterError hunterError : HunterError.values()) {
            errorCodeMap.put(hunterError.getErrorCode(), hunterError);
        }
    }

    private int errorCode;

    private HunterError(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static HunterError getErrorByCode(int errorCode) {
        return errorCodeMap.get(errorCode);
    }
}
