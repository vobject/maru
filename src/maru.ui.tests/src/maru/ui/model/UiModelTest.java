package maru.ui.model;

import maru.ui.MaruUIPlugin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UiModelTest
{
    private UiModel uiModel;

    @Before
    public void setUp() throws Exception
    {
        uiModel = MaruUIPlugin.getDefault().getUiModel();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testStartupShutdownStartup()
    {
        uiModel.startup();
        uiModel.shutdown();
        uiModel.startup();
    }
}
