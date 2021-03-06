package maru.centralbody.propertypages;

import maru.IMaruResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.bodies.OrekitCentralBody;
import maru.centralbody.preferences.MapImagesEditor;
import maru.centralbody.wizards.CentralBodyFactory;
import maru.core.model.CoreModel;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EarthPropertyPage extends UiPropertyPage
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
        return CentralBodyFactory.getMapImages(getScenarioElement().getElementName());
    }

    @Override
    public OrekitCentralBody getScenarioElement()
    {
        return (OrekitCentralBody) getUiElement().getUnderlyingElement();
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
                // try to interpret the image name as an external resource first
                image = MapImagesEditor.fromName(newImage);

                if (image == null) {
                    // try to get a bundle resource if it is no external resource
                    image = MaruCentralBodyResources.fromName(newImage);
                }
            }
            CoreModel.getDefault().changeCentralBodyImage(getScenarioElement(), image, true);
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
        OrekitCentralBody element = getScenarioElement();
        if (element == null) {
            return;
        }

        IMaruResource mapImage = element.getTexture();
        if (mapImage != null) {
            initialImage = mapImage.getName();
        } else {
            initialImage = "";
        }
    }

    private void initControls()
    {
        OrekitCentralBody element = getScenarioElement();
        if (element == null) {
            return;
        }

        IMaruResource mapImage = element.getTexture();
        if (mapImage != null) {
            images.setText(mapImage.getName());
        } else {
            images.setText("");
        }
    }
}
