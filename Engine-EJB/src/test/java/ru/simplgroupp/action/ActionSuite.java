package ru.simplgroupp.action;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        KassaBeanTest.class,
        CreditBeanTest.class,
        PaymentServiceTest.class,
        PeopleBeanTest.class
})
public class ActionSuite {
}
