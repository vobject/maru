package maru.groundstation.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.GeodeticGroundstation;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class GeodeticGroundstationPropertyPage extends UiPropertyPage
{
    private Label color;
    private Combo images;

    private RGB newColor;
    private RGB initialColor;
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
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_128.getName()
        };
    }

    @Override
    public GeodeticGroundstation getScenarioElement()
    {
        return (GeodeticGroundstation) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        GeodeticGroundstation element = getScenarioElement();

        if (newColorSelected()) {
            CoreModel.getDefault().setElementColor(element, newColor, true);
        }

        String newImage = images.getText();
        if (!newImage.equals(initialImage))
        {
            IMaruResource image = null;

            if (!newImage.isEmpty()) {
                // change the scenario's 2D graphic
                image = MaruGroundstationResources.fromName(newImage);
            }
            CoreModel.getDefault().setElementGraphics2D(element, image, true);
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

        createColorControl(container);
        createImageControl(container);

        return container;
    }

    private void createColorControl(final Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Color:");
        color = new Label(parent, SWT.BORDER);
        color.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        color.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }
            @Override public void mouseUp(MouseEvent e)
            {
                ColorDialog dlg = new ColorDialog(parent.getShell());
                dlg.setRGB(newColor);

                RGB rgb = dlg.open();
                if (rgb != null) {
                    newColor = rgb;

                    color.getBackground().dispose();
                    color.setBackground(new Color(null, newColor));
                }
            }
        });
        color.addDisposeListener(new DisposeListener() {
            @Override public void widgetDisposed(DisposeEvent e) {
                color.getBackground().dispose();
            }
        });
    }

    private void createImageControl(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Image:");
        images = new Combo(parent, SWT.READ_ONLY);
        images.setItems(getElementImageNames());
        images.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    private void initDefaults()
    {
        GeodeticGroundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        initialColor = element.getElementColor();
        newColor = initialColor;

        IMaruResource graphic2d = element.getElementGraphic2D();
        if (graphic2d != null) {
            initialImage = graphic2d.getName();
        } else {
            initialImage = "";
        }
    }

    private void initControls()
    {
        GeodeticGroundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        if (color.getBackground() != null) {
            color.getBackground().dispose();
        }
        color.setBackground(new Color(null, initialColor));

        IMaruResource graphic2d = element.getElementGraphic2D();
        if (graphic2d != null) {
            images.setText(graphic2d.getName());
        } else {
            images.setText("");
        }
    }


    private boolean newColorSelected()
    {
        return (newColor.red   != initialColor.red) ||
               (newColor.green != initialColor.green) ||
               (newColor.blue  != initialColor.blue);
    }
}
