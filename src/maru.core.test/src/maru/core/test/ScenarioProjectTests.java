package maru.core.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import maru.core.MaruCorePlugin;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.utils.TimeUtils;
import maru.core.workspace.WorkspaceModel;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.orekit.time.AbsoluteDate;

public class ScenarioProjectTests extends TestCase
{
    private WorkspaceModel workspaceModel;

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
        workspaceModel = MaruCorePlugin.getDefault().getWorkspaceModel();
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
        AbsoluteDate startTime = TimeUtils.now();
        AbsoluteDate stopTime = TimeUtils.create(startTime, 60);

        IScenarioProject scenarioProject =
                workspaceModel.createProject(project,
                                                comment,
                                                centralBody,
                                                startTime,
                                                stopTime);

        assertNotNull(scenarioProject);
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

        workspaceModel.removeProject(project, false);

        assertFalse(workspaceModel.hasProject(project));
    }
}
