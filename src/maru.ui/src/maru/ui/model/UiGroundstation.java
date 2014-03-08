package maru.ui.model;

import maru.core.model.IGroundstation;
import maru.core.model.VisibleElementColor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class UiGroundstation extends UiVisibleElement
{
    public UiGroundstation(UiGroundstationContrainer parent, IGroundstation underlying)
    {
        super(parent, underlying);
    }

    @Override
    public UiGroundstationContrainer getUiParent()
    {
        return (UiGroundstationContrainer) super.getUiParent();
    }

    @Override
    public IGroundstation getUnderlyingElement()
    {
        return (IGroundstation) super.getUnderlyingElement();
    }

    @Override
    public ImageDescriptor getImageDescriptor()
    {
        VisibleElementColor color = getUnderlyingElement().getElementColor();
        RGB rgb = new RGB(color.r, color.g, color.b);
        return ImageDescriptor.createFromImageData(new ImageData(10, 10, 1, new PaletteData(new RGB[] { rgb })));
    }
}
