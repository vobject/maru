package maru.ui.model;

import maru.core.model.ISpacecraft;
import maru.core.model.VisibleElementColor;

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
        VisibleElementColor color = getUnderlyingElement().getElementColor();
        RGB rgb = new RGB(color.r, color.g, color.b);
        return ImageDescriptor.createFromImageData(new ImageData(10, 10, 1, new PaletteData(new RGB[] { rgb })));
    }
}
