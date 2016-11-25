package ru.simplgroupp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.simplgroupp.action.ActionSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ActionSuite.class
})
public class AllSuite {
}
