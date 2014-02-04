package maru.ui.wizards;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.ui.MaruUIPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class ScenarioProjectWizard extends Wizard implements INewWizard, IExecutableExtension
{
    private static final String WIZARD_NAME = "New Scenario Project";

    private IConfigurationElement configElement;
    private IWorkbench workbench;
    private ScenarioProjectWizardPage mainPage;
    private ICentralBodyWizardPage centralBodyPage;
    private TimeframeWizardPage periodPage;

    public ScenarioProjectWizard()
    {
        setWindowTitle(WIZARD_NAME);
    }

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException
    {
        configElement = config;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        this.workbench = workbench;
    }

    @Override
    public void addPages()
    {
        mainPage = new ScenarioProjectWizardPage();
        centralBodyPage = MaruUIPlugin.getDefault().getCentralBodyWizardPage();
        periodPage = new TimeframeWizardPage();

        addPage(mainPage);
        addPage(centralBodyPage);
        addPage(periodPage);
    }

    @Override
    public boolean performFinish()
    {
        IProject project = createProject();
        if (project == null) {
            return false;
        }

        BasicNewProjectResourceWizard.updatePerspective(configElement);
        BasicNewResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());
        return true;
    }

    private IProject createProject()
    {
        IProject project = mainPage.getProjectHandle();

        try
        {
            CoreModel coreModel = CoreModel.getDefault();

            IScenarioProject scenarioProject =
                coreModel.createScenarioProject(
                    project,
                    mainPage.getComment(),
                    centralBodyPage.createCentralBody(),
                    periodPage.getStart(),
                    periodPage.getStop()
                );
            return scenarioProject.getProject();
        }
        catch (CoreException e)
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
