package maru.core.model.template;

import maru.IMaruResource;
import maru.core.model.ICentralBody;

public abstract class CentralBody extends ScenarioElement implements ICentralBody
{
    private static final long serialVersionUID = 1L;

    private IMaruResource texture;

    private double gm;
    private double equatorialRadius;
    private double flattening;

    public CentralBody(String name)
    {
        super(name);
    }

    @Override
    public IMaruResource getTexture()
    {
        return texture;
    }

    public void setTexture(IMaruResource texture)
    {
        this.texture = texture;
    }

    @Override
    public double getGM()
    {
        return gm;
    }

    @Override
    public double getEquatorialRadius()
    {
        return equatorialRadius;
    }

    @Override
    public double getFlattening()
    {
        return flattening;
    }

    public void setGM(double gm)
    {
        this.gm = gm;
    }

    public void setEquatorialRadius(double equatorialRadius)
    {
        this.equatorialRadius = equatorialRadius;
    }

    public void setFlattening(double flattening)
    {
        this.flattening = flattening;
    }
}
