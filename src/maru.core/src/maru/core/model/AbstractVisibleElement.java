package maru.core.model;

import maru.core.model.resource.IMaruResource;

public abstract class AbstractVisibleElement extends AbstractScenarioElement implements IVisibleElement
{
    private static final long serialVersionUID = 1L;

    private VisibleElementColor elementColor;
    private IMaruResource elementImage;

    public AbstractVisibleElement(String name)
    {
        super(name);

        // the default color is black
        this.elementColor = new VisibleElementColor(0, 0, 0);

        // by default objects have no image
        this.elementImage = null;
    }

    @Override
    public VisibleElementColor getElementColor()
    {
        return elementColor;
    }

    @Override
    public IMaruResource getElementImage()
    {
        return elementImage;
    }

    public void setElementColor(VisibleElementColor color)
    {
        elementColor = color;
    }

    public void setElementImage(IMaruResource elementImage)
    {
        this.elementImage = elementImage;
    }
}
