package maru.ui.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IVisibleElement;

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
import org.eclipse.swt.widgets.Label;

public abstract class UiVisiblePropertyPage extends UiPropertyPage
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

    protected abstract String[] getImageNames();
    protected abstract IMaruResource getImageFromName(String name);

    @Override
    public IVisibleElement getScenarioElement()
    {
        return (IVisibleElement) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        IVisibleElement element = getScenarioElement();

        if (newColorSelected()) {
            CoreModel.getDefault().changeColor(element, newColor, true);
        }

        String newImage = images.getText();
        if (!newImage.equals(initialImage))
        {
            IMaruResource image = null;

            if (!newImage.isEmpty()) {
                // change the scenario's 2D graphic
                image = getImageFromName(newImage);
            }
            CoreModel.getDefault().changeImage(element, image, true);
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
        images.setItems(getImageNames());
        images.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    private void initDefaults()
    {
        IVisibleElement element = getScenarioElement();
        if (element == null) {
            return;
        }

        initialColor = element.getElementColor();
        newColor = initialColor;

        IMaruResource image = element.getElementImage();
        if (image != null) {
            initialImage = image.getName();
        } else {
            initialImage = "";
        }
    }

    private void initControls()
    {
        IVisibleElement element = getScenarioElement();
        if (element == null) {
            return;
        }

        if (color.getBackground() != null) {
            color.getBackground().dispose();
        }
        color.setBackground(new Color(null, initialColor));

        IMaruResource image = element.getElementImage();
        if (image != null) {
            images.setText(image.getName());
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
