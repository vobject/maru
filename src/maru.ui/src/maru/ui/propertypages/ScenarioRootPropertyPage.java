package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ScenarioRootPropertyPage extends UiProjectPropertyPage
{
    private Text name;
    private Text comment;

    private String initialName;
    private String initialComment;

    @Override
    protected Control createContents(Composite parent)
    {
        Composite container = createControls(parent);

        initDefaults();
        initControls();

        noDefaultAndApplyButton();
        return container;
    }

    @Override
    public boolean performOk()
    {
        String newName = name.getText();
        if (!newName.equals(initialName)) {
            // changing the name of a scenario is currently not supported

            // TODO: change the projects name
        }

        String newComment = comment.getText();
        if (!newComment.equals(initialComment)) {
            // change the scenario's comment
            CoreModel.getDefault().commentElement(getScenario(), newComment, true);
        }

        return true;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        createNameControl(container);
        createCommentControl(container);

        return container;
    }

    private void createNameControl(Composite parent)
    {
        // create scenario name label and control
        // a scenario's name cannot be changed

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.BEGINNING;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;

        new Label(parent, SWT.NONE).setText("Name:");
        name = new Text(parent, SWT.WRAP | SWT.READ_ONLY);
        name.setLayoutData(data);
    }

    private void createCommentControl(Composite parent)
    {
        // create scenario comment label and control
        // a scenario's comment can be changed with this property page

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        new Label(parent, SWT.NONE).setText("Comment:");
        comment = new Text(parent, SWT.BORDER | SWT.MULTI);
        comment.setLayoutData(data);
    }

    private void initDefaults()
    {
        IScenarioProject scenario = getScenario();
        if (scenario == null) {
            return;
        }

        initialName = scenario.getElementName();
        initialComment = scenario.getElementComment();
    }

    private void initControls()
    {
        IScenarioProject scenario = getScenario();
        if (scenario == null) {
            return;
        }

        name.setText(scenario.getElementName());
        comment.setText(scenario.getElementComment());
    }
}