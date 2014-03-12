package maru.core.workspace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModelMock;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.utils.TimeUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.orekit.time.AbsoluteDate;

public class WorkspaceModelTest
{
    private WorkspaceModel workspaceModel;

    @Before
    public void setUp() throws Exception
    {
        workspaceModel = MaruCorePlugin.getDefault().getWorkspaceModel();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testCreateAndRemoveScenarioProject() throws CoreException
    {
        String projectName = "testCreateAndRemoveScenarioProject";

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        ICentralBody centralBody = new CoreModelMock.DummyCentralBody();
        String comment = "TestScenarioProject Comment";
        AbsoluteDate startTime = TimeUtils.now();
        AbsoluteDate stopTime = TimeUtils.create(startTime, 60);

        IScenarioProject scenarioProject =
            workspaceModel.createProject(project, comment, centralBody,
                                         startTime, stopTime);

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
