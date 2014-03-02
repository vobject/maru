package maru.ui.wizards;

import maru.core.model.IScenarioProject;
import maru.core.workspace.WorkspaceModel;
import maru.ui.model.UiElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public abstract class ScenarioElementWizard extends Wizard implements INewWizard
{
    private IStructuredSelection selection;

    public ScenarioElementWizard(String wizardName)
    {
        setWindowTitle(wizardName);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        if (selection == null || selection.isEmpty()) {
            return;
        }
        this.selection = selection;
    }

    protected IScenarioProject getScenarioProjectFromSelection()
    {
        if (selection == null) {
            return null;
        }
        Object selectedObject = selection.getFirstElement();

        // we must know which project to assign a new object to
        IProject parentProject = null;

        if (selectedObject instanceof IProject) {
            parentProject = (IProject) selectedObject;
        } else if (selectedObject instanceof UiElement) {
            IScenarioProject scenario = ((UiElement) selectedObject).getUiProject().getUnderlyingElement();
            parentProject = WorkspaceModel.getDefault().getProject(scenario);
        }
        return WorkspaceModel.getDefault().getProject(parentProject);
    }

    public static void createLine(Composite parent, int columns)
    {
        Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = columns;
        line.setLayoutData(gridData);
    }
}
