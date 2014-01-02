package maru.spacecraft.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.OrekitSpacecraft;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class OrekitSpacecraftPropertyPage extends UiPropertyPage
{
    private Combo images;
    private String initialImage;

    public Combo getImageControl()
    {
        return images;
    }

    public String getInitialImage()
    {
        return initialImage;
    }

    protected String[] getElementImageNames()
    {
        // return an empty array be default
        return new String[] {
            "", // empty string allows to disable element image
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_128.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_128.getName()
        };
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

        String newImage = images.getText();
        if (!newImage.equals(initialImage))
        {
            IMaruResource image = null;

            if (!newImage.isEmpty()) {
                // change the scenario's 2D graphic
                image = MaruSpacecraftResources.fromName(newImage);
            }
            CoreModel.getDefault().setElementGraphics2D(getScenarioElement(), image, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Control createContents(Composite parent)
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
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        createImageControl(container);

        return container;
    }

    private void createImageControl(Composite parent)
    {
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.BEGINNING;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;

        new Label(parent, SWT.NONE).setText("Image:");
        images = new Combo(parent, SWT.READ_ONLY);
        images.setItems(getElementImageNames());
        images.setLayoutData(data);
    }

    private void initDefaults()
    {
        OrekitSpacecraft element = getScenarioElement();
        if (element == null) {
            return;
        }

        IMaruResource graphic2d = element.getElementGraphic2D();
        if (graphic2d != null) {
            initialImage = graphic2d.getName();
        } else {
            initialImage = "";
        }
    }

    private void initControls()
    {
        OrekitSpacecraft element = getScenarioElement();
        if (element == null) {
            return;
        }

        IMaruResource graphic2d = element.getElementGraphic2D();
        if (graphic2d != null) {
            images.setText(graphic2d.getName());
        } else {
            images.setText("");
        }
    }
}
