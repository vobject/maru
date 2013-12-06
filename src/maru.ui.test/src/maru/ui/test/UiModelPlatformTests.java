package maru.ui.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import maru.ui.MaruUIPlugin;
import maru.ui.model.UiModel;

public class UiModelPlatformTests extends TestCase
{
    private UiModel uiModel;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        return new TestSuite(UiModelPlatformTests.class);
    }

    public UiModelPlatformTests(String name)
    {
        super(name);
    }

    @Override
    protected void setUp() throws Exception
    {
        uiModel = MaruUIPlugin.getDefault().getUiModel();
    }

    @Override
    protected void tearDown() throws Exception
    {

    }

    public void testStartupShutdownStartup()
    {
        uiModel.startup();
        uiModel.shutdown();
        uiModel.startup();
    }
}
