package maru.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class ScenarioProjectWizardPage extends WizardNewProjectCreationPage
{
    private static final String PAGE_NAME = "Scenario Project Wizard";
    private static final String PAGE_TITLE = "Scenario";
    private static final String PAGE_DESCRIPTION = "Create a new scenario.";

    private Text comment;

    public ScenarioProjectWizardPage()
    {
        super(PAGE_NAME);
        setTitle(PAGE_TITLE);
        setDescription(PAGE_DESCRIPTION);
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());

        // default entries so dummy scenarios can be created quicker
        setInitialProjectName("New Scenario");

        // create the default new-project controls
        super.createControl(container);
        createLine(container, 1);

        Composite subContainer = new Composite(container, SWT.NONE);
        subContainer.setLayout(new GridLayout(2, false));
        subContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        new Label(subContainer, SWT.NONE).setText("Comment:");
        comment = new Text(subContainer, SWT.BORDER | SWT.MULTI);
        comment.setText("Comment");
        comment.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        setControl(container);
    }

    public String getComment()
    {
        return comment.getText();
    }

    private static void createLine(Composite parent, int columns)
    {
        Label line = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = columns;
        line.setLayoutData(gridData);
    }
}
