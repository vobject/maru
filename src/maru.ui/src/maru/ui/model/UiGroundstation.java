package maru.ui.model;

import maru.core.model.IGroundstation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class UiGroundstation extends UiVisible
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
        return ImageDescriptor.createFromImageData(new ImageData(10, 10, 1, new PaletteData(new RGB[] { getUnderlyingElement().getElementColor() })));
    }
}
