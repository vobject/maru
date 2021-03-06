package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.ui.model.UiElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UiElementPropertyPage extends UiPropertyPage
{
    private Text name;
    private Text comment;

    private String initialName;
    private String initialComment;

    public Text getNameControl()
    {
        return name;
    }

    public Text getCommentControl()
    {
        return comment;
    }

    public String getInitialName()
    {
        return initialName;
    }

    public String getInitialComment()
    {
        return initialComment;
    }

    @Override
    public boolean performOk()
    {
        String newName = name.getText();
        if (!newName.equals(initialName)) {
            CoreModel.getDefault().changeElementName(getScenarioElement(), newName, true);
        }

        String newComment = comment.getText();
        if (!newComment.equals(initialComment)) {
            // change the scenario's comment
            CoreModel.getDefault().changeElementComment(getScenarioElement(), newComment, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        Composite container = createControls(parent);

        initDefaults();
        initControls();

        return container;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = SWT.FILL;
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
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.BEGINNING;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;

        new Label(parent, SWT.NONE).setText("Name:");
        name = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        name.setLayoutData(data);
    }

    private void createCommentControl(Composite parent)
    {
        // create scenario comment label and control
        // a scenario's comment can be changed with this property page

        GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        new Label(parent, SWT.NONE).setText("Comment:");
        comment = new Text(parent, SWT.BORDER | SWT.MULTI);
        comment.setLayoutData(data);
    }

    private void initDefaults()
    {
        UiElement element = getUiElement();
        if (element == null) {
            return;
        }

        initialName = element.getName();
        initialComment = element.getComment();
    }

    private void initControls()
    {
        UiElement element = getUiElement();
        if (element == null) {
            return;
        }

        name.setText(element.getName());
        comment.setText(element.getComment());
    }
}