package maru.spacecraft.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.orekit.orbits.Orbit;

public abstract class OrbitControls
{
    private Composite container;
    private Label description;
    private String errorMsg = "";

    public OrbitControls(Composite parent)
    {
        createContainer(parent);
        createControls();
    }

    public OrbitControls(Composite parent, Orbit orbit)
    {
        this(parent);
    }

    public Composite getContainer()
    {
        return container;
    }

    public void setContainer(Composite container)
    {
        this.container = container;
    }

    public String getDescription()
    {
        return description.getText();
    }

    public void setDescription(String description)
    {
        this.description.setText(description);
    }

    public String getErrorMessage()
    {
        return errorMsg;
    }

    public void setErrorMessage(String msg)
    {
        this.errorMsg = msg;
    }

    public void dispose()
    {
        if (container != null) {
            container.dispose();
            container = null;
        }
    }

    public abstract Orbit getOrbit();
    public abstract boolean isValid();
    public abstract boolean isModified();
    public abstract void refreshDefaults(Orbit orbit);

    protected abstract void createControls();

    private void createContainer(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        GridData descriptionData = new GridData(SWT.FILL, SWT.FILL, true, false);
        descriptionData.horizontalSpan = 2;
        description = new Label(container, SWT.BORDER | SWT.WRAP);
        description.setLayoutData(descriptionData);

        GridData lineData = new GridData(SWT.FILL, SWT.FILL, true, false);
        lineData.horizontalSpan = 2;
        Label line = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BOLD);
        line.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        line.setLayoutData(lineData);

        setContainer(container);
    }
}
