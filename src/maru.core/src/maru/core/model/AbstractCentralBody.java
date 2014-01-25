package maru.core.model;

import java.util.Map;

import maru.IMaruResource;

import org.orekit.frames.Frame;

public abstract class AbstractCentralBody extends AbstractScenarioElement implements ICentralBody
{
    private static final long serialVersionUID = 1L;

    private IMaruResource texture;

    private double gm;
    private double equatorialRadius;
    private double flattening;

    private Frame frame;

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

    @Override
    public Frame getFrame()
    {
        return frame;
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

    public void setFrame(Frame frame)
    {
        this.frame = frame;
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
