package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.ui.model.UiElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UiElementPropertyPage extends UiPropertyPage
{
    private Text name;
    private Text comment;
//    private Combo images;

    private String initialName;
    private String initialComment;
//    private String initialImage;

    public Text getNameControl()
    {
        return name;
    }

    public Text getCommentControl()
    {
        return comment;
    }

//    public Combo getImageControl()
//    {
//        return images;
//    }

    public String getInitialName()
    {
        return initialName;
    }

    public String getInitialComment()
    {
        return initialComment;
    }

//    public String getInitialImage()
//    {
//        return initialImage;
//    }

    protected String[] getElementImageNames()
    {
        // return an empty array be default
        return new String[] { };
    }

    @Override
    protected Control createContents(Composite parent)
    {
        Composite container = createControls(parent);

        initDefaults();
        initControls();

        return container;
    }

    @Override
    public boolean performOk()
    {
        String newName = name.getText();
        if (!newName.equals(initialName)) {
            CoreModel.getDefault().renameElement(getScenarioElement(), newName, true);
        }

        String newComment = comment.getText();
        if (!newComment.equals(initialComment)) {
            // change the scenario's comment
            CoreModel.getDefault().commentElement(getScenarioElement(), newComment, true);
        }

//        String newImage = images.getText();
//        if (!newImage.equals(initialImage)) {
//            // change the scenario's 2D graphic
//            // TODO
////            CoreModel.getDefault().setElementGraphics2D(getScenarioElement(), null, true);
//        }

        return true;
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

        createNameControl(container);
        createCommentControl(container);
//        createImageControl(container);

        return container;
    }

    private void createNameControl(Composite parent)
    {
        // create scenario name label and control
        // a scenario's name cannot be changed

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.BEGINNING;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;

        new Label(parent, SWT.NONE).setText("Name:");
        name = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        name.setLayoutData(data);
    }

    private void createCommentControl(Composite parent)
    {
        // create scenario comment label and control
        // a scenario's comment can be changed with this property page

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        new Label(parent, SWT.NONE).setText("Comment:");
        comment = new Text(parent, SWT.BORDER | SWT.MULTI);
        comment.setLayoutData(data);
    }

//    private void createImageControl(Composite parent)
//    {
//        GridData data = new GridData();
//        data.horizontalAlignment = GridData.FILL;
//        data.verticalAlignment = GridData.BEGINNING;
//        data.grabExcessHorizontalSpace = true;
//        data.grabExcessVerticalSpace = false;
//
//        new Label(parent, SWT.NONE).setText("Image:");
//        images = new Combo(parent, SWT.READ_ONLY);
//        images.setItems(getElementImageNames());
//        images.setLayoutData(data);
//    }

    private void initDefaults()
    {
        UiElement element = getUiElement();
        if (element == null) {
            return;
        }

        initialName = element.getName();
        initialComment = element.getComment();
//        initialImage = element.getUnderlyingElement().getElementGraphic2D().getName();
    }

    private void initControls()
    {
        UiElement element = getUiElement();
        if (element == null) {
            return;
        }

        name.setText(element.getName());
        comment.setText(element.getComment());
//        images.setText(element.getUnderlyingElement().getElementGraphic2D().getName());
    }
}