package maru.centralbody.propertypages;

import maru.IMaruResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.earth.Earth;
import maru.centralbody.preferences.MapImagesEditor;
import maru.centralbody.wizards.CentralBodyFactory;
import maru.core.model.CoreModel;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
    public Earth getScenarioElement()
    {
        return (Earth) getUiElement().getUnderlyingElement();
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
        Earth element = getScenarioElement();
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
        Earth element = getScenarioElement();
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
