package maru.core.model;

import java.util.Map;

import maru.core.model.resource.IMaruResource;

public abstract class AbstractCentralBody extends AbstractScenarioElement implements ICentralBody
{
    private static final long serialVersionUID = 1L;

    private IMaruResource texture;

    private double gm;
    private double equatorialRadius;
    private double flattening;

    public AbstractCentralBody(String name)
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

    public void setProperties(double equatorialRadius, double flattening, double gm)
    {
        this.equatorialRadius = equatorialRadius;
        this.flattening = flattening;
        this.gm = gm;
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> props = super.getPropertyMap();

        props.put("GM (m\u00b3/s\u00b2)", Double.toString(getGM()));
        props.put("Radius (m)", Double.toString(getEquatorialRadius()));
        props.put("Flattening", Double.toString(getFlattening()));
        props.put("Frame", getFrame().toString());

        return props;
    }
}
