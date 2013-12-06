package maru.ui.wizards;

import maru.core.model.IScenarioProject;

import org.eclipse.jface.wizard.WizardPage;

public abstract class ScenarioElementWizardPage extends WizardPage
{
    private final IScenarioProject project;

    public ScenarioElementWizardPage(String pageName,
                                     String pageTitle,
                                     String pageDescription,
                                     IScenarioProject project)
    {
        super(pageName);
        setTitle(pageTitle);
        setDescription(pageDescription);

        this.project = project;
    }

    public IScenarioProject getProject()
    {
        return project;
    }
}
