package maru.ui.wizards;

import java.util.Random;

import maru.core.model.IScenarioProject;
import maru.core.model.VisibleElementColor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class ScenarioElementWizardNamingPage extends ScenarioElementWizardPage
{
    private Text name;
    private Text comment;
    private Label colorLabel;
    private RGB color;
    private Combo image;

    protected final KeyListener inputValidation = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            setPageComplete(isInputValid());
        }
    };

    public ScenarioElementWizardNamingPage(String pageName,
                                           String pageTitle,
                                           String pageDescription,
                                           IScenarioProject project)
    {
        super(pageName, pageTitle, pageDescription, project);

        color = getRandomColor();
    }

    @Override
    public void createControl(final Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Name:");
        name = new Text(parent, SWT.BORDER | SWT.SINGLE);
        name.addKeyListener(inputValidation);
        name.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(parent, SWT.NONE).setText("Description:");
        comment = new Text(parent, SWT.BORDER | SWT.MULTI);
        comment.addKeyListener(inputValidation);
        comment.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        new Label(parent, SWT.NONE).setText("Color:");
        colorLabel = new Label(parent, SWT.BORDER);
        colorLabel.setBackground(new Color(null, color));
        colorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        colorLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) { }
            @Override
            public void mouseDown(MouseEvent e) { }
            @Override
            public void mouseUp(MouseEvent e) {
                ColorDialog dlg = new ColorDialog(parent.getShell());
                dlg.setRGB(color);

                RGB rgb = dlg.open();
                if (rgb != null) {
                    color = rgb;

                    colorLabel.getBackground().dispose();
                    colorLabel.setBackground(new Color(null, color));
                }
            }
        });
        colorLabel.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                colorLabel.getBackground().dispose();
            }
        });

        new Label(parent, SWT.NONE).setText("Image:");
        image = new Combo(parent, SWT.READ_ONLY);
        image.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        String[] imagePathItems = getElementImages();
        if (imagePathItems != null && imagePathItems.length > 0) {
            image.setItems(imagePathItems);
        } else {
            image.setEnabled(false);
        }
    }

    public String getElementName()
    {
        return name.getText().trim();
    }

    public String getElementComment()
    {
        return comment.getText().trim();
    }

    public VisibleElementColor getElementColor()
    {
        return new VisibleElementColor(color.red, color.green, color.blue);
    }

    public String getElementImage()
    {
        return image.getText();
    }

    protected Text getNameControl()
    {
        return name;
    }

    protected Text getCommentControl()
    {
        return comment;
    }

    protected Label getColorControl()
    {
        return colorLabel;
    }

    protected Combo getImageControl()
    {
        return image;
    }

    protected abstract String[] getElementImages();

    protected boolean isInputValid()
    {
        // check if we have a valid scenario that the new object
        // should be added to. it is needed later to verify that no two
        // objects with the same name get added.
        if (getProject() == null) {
            setErrorMessage("No scenario selected in the workspace.");
            return false;
        }

        if (getElementName().isEmpty()) {
            setErrorMessage("The object must have a name.");
            return false;
        }

        // the comment may be empty
        return true;
    }

    private RGB getRandomColor()
    {
        Random generator = new Random();
        return new RGB(generator.nextInt(255),  // red
                       generator.nextInt(255),  // green
                       generator.nextInt(255)); // blue
    }
}
