package maru.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCorePlatformTests
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(AllCorePlatformTests.class.getName());

        // tests that need a running eclipse platform
        suite.addTest(CoreModelPlatformTests.suite());
        suite.addTest(ScenarioProjectTests.suite());

        return suite;
    }
}
