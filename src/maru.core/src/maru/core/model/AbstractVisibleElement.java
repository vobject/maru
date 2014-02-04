package maru.core.model;

import maru.IMaruResource;

import org.eclipse.swt.graphics.RGB;

public abstract class AbstractVisibleElement extends AbstractScenarioElement implements IVisibleElement
{
    private static final long serialVersionUID = 1L;

    private RGB elementColor;
    private IMaruResource elementImage;

    public AbstractVisibleElement(String name)
    {
        super(name);

        // the default color is black
        this.elementColor = new RGB(0, 0, 0);

        // by default objects have no image
        this.elementImage = null;
    }

    @Override
    public RGB getElementColor()
    {
        return elementColor;
    }

    @Override
    public IMaruResource getElementImage()
    {
        return elementImage;
    }

    public void setElementColor(RGB color)
    {
        elementColor = color;
    }

    public void setElementImage(IMaruResource elementImage)
    {
        this.elementImage = elementImage;
    }
}
