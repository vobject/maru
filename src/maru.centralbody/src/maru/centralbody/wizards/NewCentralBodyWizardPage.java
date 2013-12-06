package maru.centralbody.wizards;

import java.util.ArrayList;

import maru.IMaruResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.earth.Earth;
import maru.centralbody.preferences.MapImagesEditor;
import maru.core.model.ICentralBody;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The central bodies supported by the wizard page.
 */
enum CentralBodies
{
    Earth;

    ICentralBody createCentralBody(String imageName)
    {
        return createCentralBody(this.toString(), imageName);
    }

    static ICentralBody createCentralBody(String name, String imageName)
    {
        switch (CentralBodies.valueOf(name))
        {
            case Earth:
            {
                // try to interpret the image name as an external resource first
                IMaruResource res = MapImagesEditor.fromName(imageName);

                if (res == null) {
                    // try to get a bundle resource if it is no external resource
                    res = MaruCentralBodyResources.fromName(imageName);
                }

                return new Earth(res);
            }
        }
        return null;
    }

    static String[] getMapImages(String name)
    {
        ArrayList<String> images = new ArrayList<>();

        switch (CentralBodies.valueOf(name))
        {
            case Earth:
            {
                // add predefined map images
                images.add(MaruCentralBodyResources.MAP_EARTH_1.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_2.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_3.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_4.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_5.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_6.getName());
            }
        }

        // FIXME: external map images are currently not assigned to a specific
        // central body of any sort. they are added to all available bodies.

        // add external map images to selection
        for (IMaruResource res : MapImagesEditor.getMapImageResources()) {
            images.add(res.getName());
        }

        return images.toArray(new String[images.size()]);
    }
}

/**
 * Wizard page selecting, reviewing, and creating a central body for a
 * scenario. It supports only {@link maru.centralbody.earth.Earth}.
 * <p>
 * This wizard page serves no actual purpose at the moment. It is there to
 * present the configuration of the central body that will be created, but the
 * properties cannot be changed. This would have to change as soon as new
 * central bodies can be selected in addition to {@link maru.centralbody.earth.Earth}.
 */
public class NewCentralBodyWizardPage extends WizardPage
{
    private static final String PAGE_NAME = "Central Body Page";
    private static final String PAGE_TITLE = "Central Body";
    private static final String PAGE_DESCRIPTION = "Choose the central body for the new scenario.";

    /** Names of the central body to be used. */
    private Combo centralBody;

    /** Names of the map images for the central body. */
    private Combo images;

    private Text equatorialRadius;
    private Text flattening;
    private Text frame;
    private Text gm;

    public NewCentralBodyWizardPage()
    {
        super(PAGE_NAME);
        setTitle(PAGE_TITLE);
        setDescription(PAGE_DESCRIPTION);
    }

    @Override
    public void createControl(final Composite parent)
    {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));

        new Label(container, SWT.NONE).setText("Central body:");
        centralBody = new Combo(container, SWT.READ_ONLY);
        centralBody.setItems(getAvailableBodies());
        centralBody.select(0);
        centralBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Map image:");
        images = new Combo(container, SWT.READ_ONLY);
        images.setItems(getMapImages());
        images.select(0);
        images.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        ICentralBody initialBody = createCentralBody();

        new Label(container, SWT.NONE).setText("Equatorial Radius (km):");
        equatorialRadius = new Text(container, SWT.BORDER);
        equatorialRadius.setText(Double.toString(initialBody.getEquatorialRadius() / 1000.0));
        equatorialRadius.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        equatorialRadius.setEnabled(false);

        new Label(container, SWT.NONE).setText("Flattening:");
        flattening = new Text(container, SWT.BORDER);
        flattening.setText(Double.toString(initialBody.getFlattening()));
        flattening.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        flattening.setEnabled(false);

        new Label(container, SWT.NONE).setText("Frame:");
        frame = new Text(container, SWT.BORDER);
        frame.setText(initialBody.getFrame().toString());
        frame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        frame.setEnabled(false);

        new Label(container, SWT.NONE).setText("GM (m\u00b3/s\u00b2):");
        gm = new Text(container, SWT.BORDER);
        gm.setText(Double.toString(initialBody.getGM()));
        gm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        gm.setEnabled(false);

        setControl(container);
    }

    public ICentralBody createCentralBody()
    {
        return CentralBodies.createCentralBody(centralBody.getText(), images.getText());
    }

    private String[] getAvailableBodies()
    {
        return new String[] {
            CentralBodies.Earth.toString()
        };
    }

    private String[] getMapImages()
    {
        return CentralBodies.getMapImages(centralBody.getText());
    }
}
