package maru.ui.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUiPlatformTests
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(AllUiPlatformTests.class.getName());

        // tests that need a running eclipse platform
        suite.addTest(UiModelPlatformTests.suite());
        suite.addTest(TimelinePlatformTests.suite());

        return suite;
    }
}
