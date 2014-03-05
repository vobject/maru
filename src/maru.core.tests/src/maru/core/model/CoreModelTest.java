package maru.core.model;

import maru.core.MaruCorePlugin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoreModelTest
{
    private CoreModel coreModel;

    @Before
    public void setUp() throws Exception
    {
        coreModel = MaruCorePlugin.getDefault().getCoreModel();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testStartupShutdownStartup()
    {
        coreModel.startup();
        coreModel.shutdown();
        coreModel.startup();
    }
}
