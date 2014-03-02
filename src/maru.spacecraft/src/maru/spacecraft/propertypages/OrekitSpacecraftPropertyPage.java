package maru.spacecraft.propertypages;

import maru.core.model.resource.IMaruResource;
import maru.spacecraft.model.OrekitSpacecraft;
import maru.spacecraft.model.SpacecraftResources;
import maru.ui.propertypages.UiVisiblePropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class OrekitSpacecraftPropertyPage extends UiVisiblePropertyPage
{
    @Override
    protected String[] getImageNames()
    {
        // return an empty array be default
        return new String[] {
            "", // empty string allows to disable element image
            SpacecraftResources.SPACECRAFT_DEFAULT_1.getName(),
            SpacecraftResources.SPACECRAFT_DEFAULT_2.getName(),
            SpacecraftResources.SPACECRAFT_DEFAULT_3.getName(),
            SpacecraftResources.SPACECRAFT_ISS_1.getName(),
            SpacecraftResources.SPACECRAFT_ISS_2.getName(),
            SpacecraftResources.SPACECRAFT_ASTRONAUT_1.getName(),
            SpacecraftResources.SPACECRAFT_ROCKET_1.getName(),
            SpacecraftResources.SPACECRAFT_SHUTTLE_1.getName(),
            SpacecraftResources.SPACECRAFT_SHUTTLE_2.getName(),
        };
    }

    @Override
    protected IMaruResource getImageFromName(String name)
    {
        return SpacecraftResources.fromName(name);
    }

    @Override
    public OrekitSpacecraft getScenarioElement()
    {
        return (OrekitSpacecraft) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        // TODO: SC specific stuff

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        Composite container = super.createContents(parent);
        addSeparator(container);

        createControls(container);

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
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        // TODO: Add SC specific controls

        return container;
    }

    private void initDefaults()
    {
        OrekitSpacecraft element = getScenarioElement();
        if (element == null) {
            return;
        }

        // TODO: init SC specific variables
    }

    private void initControls()
    {
        OrekitSpacecraft element = getScenarioElement();
        if (element == null) {
            return;
        }

        // TODO: init SC specific controls
    }
}
