package maru.core.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;

public class CoreModelPlatformTests extends TestCase
{
    private CoreModel coreModel;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        return new TestSuite(CoreModelPlatformTests.class);
    }

    public CoreModelPlatformTests(String name)
    {
        super(name);
    }

    @Override
    protected void setUp() throws Exception
    {
        coreModel = MaruCorePlugin.getDefault().getCoreModel();
    }

    @Override
    protected void tearDown() throws Exception
    {

    }

    public void testStartupShutdownStartup()
    {
        coreModel.startup();
        coreModel.shutdown();
        coreModel.startup();
    }
}
