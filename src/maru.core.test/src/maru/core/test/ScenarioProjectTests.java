package maru.core.test;

import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class ScenarioProjectTests extends TestCase
{
    private CoreModel coreModel;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        return new TestSuite(ScenarioProjectTests.class);
    }

    public ScenarioProjectTests(String name)
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

    public void testCreateAndRemoveScenarioProject() throws CoreException
    {
        String projectName = "testCreateAndRemoveScenarioProject";

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        ICentralBody centralBody = new CoreModelMock.DummyCentralBody();
        String comment = "TestScenarioProject Comment";
        long startTime = new Date().getTime() / 1000;
        long stopTime = startTime + 60;

        IScenarioProject scenarioProject =
                coreModel.createScenarioProject(project,
                                                comment,
                                                centralBody,
                                                startTime,
                                                stopTime);

        assertNotNull(scenarioProject);
        assertNotNull(scenarioProject.getProject());
        assertNotNull(scenarioProject.getScenarioProject());
        assertEquals(scenarioProject.getElementName(), projectName);
        assertEquals(scenarioProject.getElementComment(), comment);

        assertNotNull(scenarioProject.getGroundstationContainer());
        assertNotNull(scenarioProject.getSpacecraftContainer());
        assertNotNull(scenarioProject.getCentralBody());
        assertNotNull(scenarioProject.getStartTime());
        assertNotNull(scenarioProject.getStopTime());
        assertNotNull(scenarioProject.getCurrentTime());
        assertEquals(scenarioProject.getTimepoints().size(), 2);

        coreModel.removeProject(project, false);

        assertFalse(coreModel.hasProject(project));
    }
}
