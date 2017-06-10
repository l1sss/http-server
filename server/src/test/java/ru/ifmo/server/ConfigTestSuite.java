package ru.ifmo.server;

import junit.framework.TestSuite;

/**
 * Created by Тарас on 09.06.2017.
 */
public class ConfigTestSuite{
    public static TestSuite suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new TestSuite(PathPropTest.class));
        suite.addTest(new TestSuite(PathXmlTest.class));
        suite.addTest(new TestSuite(NoPathTest.class));

        return suite;
    }
}
