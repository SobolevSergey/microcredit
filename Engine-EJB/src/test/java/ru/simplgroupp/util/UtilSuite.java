package ru.simplgroupp.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestRemoteDB.class,
        TestSoapUtils.class
})
public class UtilSuite {
}
