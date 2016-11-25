package ru.simplgroupp.exception;

import java.util.Locale;


public interface D3ExceptionInterface {
    String getModuleCode();
    String getErrorCode();
    String getMessage();
    String getLocalizedMessage(Locale aLocale);
    String getCompleteMessage();
    String getCompleteLocalizedMessage(Locale aLocale);
    Object[] getMessageArguments();
}
