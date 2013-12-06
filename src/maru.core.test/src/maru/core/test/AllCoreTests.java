package maru.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCoreTests
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(AllCoreTests.class.getName());

        // tests that do not depend on a running eclipse platform

        return suite;
    }
}
