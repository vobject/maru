package maru.ui.model;

import maru.core.model.ISpacecraft;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class UiSpacecraft extends UiVisible
{
    public UiSpacecraft(UiSpacecraftContrainer parent, ISpacecraft underlying)
    {
        super(parent, underlying);
    }

    @Override
    public UiSpacecraftContrainer getUiParent()
    {
        return (UiSpacecraftContrainer) super.getUiParent();
    }

    @Override
    public ISpacecraft getUnderlyingElement()
    {
        return (ISpacecraft) super.getUnderlyingElement();
    }

    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return ImageDescriptor.createFromImageData(new ImageData(10, 10, 1, new PaletteData(new RGB[] { getUnderlyingElement().getElementColor() })));
    }
}
