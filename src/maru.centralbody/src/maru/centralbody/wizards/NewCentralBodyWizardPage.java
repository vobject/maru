package maru.centralbody.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import maru.centralbody.model.CentralBodyResources;
import maru.core.model.ICentralBody;
import maru.ui.wizards.ICentralBodyWizardPage;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.orekit.errors.OrekitException;

/**
 * Wizard page selecting, reviewing, and creating a central body for a
 * scenario. It supports only {@link maru.centralbody.model.OrekitCentralBody}.
 * <p>
 * This wizard page serves no actual purpose at the moment. It is there to
 * present the configuration of the central body that will be created, but the
 * properties cannot be changed. This would have to change as soon as new
 * central bodies can be selected in addition to {@link maru.centralbody.model.OrekitCentralBody}.
 */
public class NewCentralBodyWizardPage extends WizardPage implements ICentralBodyWizardPage
{
    private static final String PAGE_NAME = "Central Body Page";
    private static final String PAGE_TITLE = "Central Body";
    private static final String PAGE_DESCRIPTION = "Choose the central body for the new scenario.";

    /** Names of the central body to be used. */
    private Combo centralBody;

    /** Names of the map images for the central body. */
    private Combo images;
    private Link imageUrl;

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
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));

        new Label(container, SWT.NONE).setText("Central body:");
        centralBody = new Combo(container, SWT.READ_ONLY);
        centralBody.setItems(getAvailableBodies());
        centralBody.select(0);
        centralBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        centralBody.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                images.setItems(getMapImages());
                images.select(0);

                ICentralBody initialBody;
                try {
                    initialBody = createCentralBody();
                } catch (OrekitException ex) {
                    throw new RuntimeException("Failed to create a central body instance.", ex);
                }

                if (initialBody.getTexture() instanceof CentralBodyResources) {
                    String url = ((CentralBodyResources) initialBody.getTexture()).getUrl();
                    imageUrl.setText("<a href=\"" + url + "\">" + url + "</a>");
                } else {
                    imageUrl.setText("<custom>");
                }
            }
        });

        new Label(container, SWT.NONE).setText("Map image:");
        images = new Combo(container, SWT.READ_ONLY);
        images.setItems(getMapImages());
        images.select(0);
        images.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        images.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ICentralBody initialBody;
                try {
                    initialBody = createCentralBody();
                } catch (OrekitException ex) {
                    throw new RuntimeException("Failed to create a central body instance.", ex);
                }

                if (initialBody.getTexture() instanceof CentralBodyResources) {
                    String url = ((CentralBodyResources) initialBody.getTexture()).getUrl();
                    imageUrl.setText("<a href=\"" + url + "\">" + url + "</a>");
                } else {
                    imageUrl.setText("<custom>");
                }
            }
        });

        new Label(container, SWT.NONE).setText("Map image source:");
        imageUrl = new Link(container, SWT.BORDER);
        imageUrl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        imageUrl.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               try {
                    PlatformUI.getWorkbench().getBrowserSupport()
                                             .getExternalBrowser()
                                             .openURL(new URL(e.text));
               } catch (PartInitException | MalformedURLException ex) {
                   ex.printStackTrace();
               }
            }
        });

        ICentralBody initialBody;
        try {
            initialBody = createCentralBody();
        } catch (OrekitException e) {
            throw new RuntimeException("Failed to create a central body instance.", e);
        }

        if (initialBody.getTexture() instanceof CentralBodyResources) {
            String url = ((CentralBodyResources) initialBody.getTexture()).getUrl();
            imageUrl.setText("<a href=\"" + url + "\">" + url + "</a>");
        } else {
            imageUrl.setText("<custom>");
        }

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

        new Label(container, SWT.NONE).setText("GM (m\u00b3/s\u00b2):");
        gm = new Text(container, SWT.BORDER);
        gm.setText(Double.toString(initialBody.getGM()));
        gm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        gm.setEnabled(false);

        new Label(container, SWT.NONE).setText("Frame:");
        frame = new Text(container, SWT.BORDER);
        frame.setText(initialBody.getFrame().toString());
        frame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        frame.setEnabled(false);

        setControl(container);
    }

    @Override
    public ICentralBody createCentralBody() throws OrekitException
    {
        return CentralBodyFactory.createCentralBody(centralBody.getText(), images.getText());
    }

    private String[] getAvailableBodies()
    {
        return new String[] {
            CentralBodyFactory.Earth.toString(),
//            CentralBodyFactory.Moon.toString(),
        };
    }

    private String[] getMapImages()
    {
        return CentralBodyFactory.getMapImages(centralBody.getText());
    }
}
