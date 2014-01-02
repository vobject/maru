package maru.core.model.template;

import maru.core.model.ICentralBody;

public abstract class CentralBody extends ScenarioElement implements ICentralBody
{
    private static final long serialVersionUID = 1L;

    private double gm;
    private double equatorialRadius;
    private double polarRadius;
    private double meanRadius;
    private double flattening;

    public CentralBody(String name)
    {
        super(name);
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
    public double getPolarRadius()
    {
        return polarRadius;
    }

    @Override
    public double getMeanRadius()
    {
        return meanRadius;
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

    public void setPolarRadius(double polarRadius)
    {
        this.polarRadius = polarRadius;
    }

    public void setMeanRadius(double meanRadius)
    {
        this.meanRadius = meanRadius;
    }

    public void setFlattening(double flattening)
    {
        this.flattening = flattening;
    }
}
