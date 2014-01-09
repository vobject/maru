package maru.core.model.template;

import java.util.LinkedHashMap;
import java.util.Map;

import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;

public abstract class ScenarioElement implements IScenarioElement
{
    private static final long serialVersionUID = 1L;

    private IScenarioElement parent;
    private String name;
    private String comment;

    public ScenarioElement(String name)
    {
        this.name = name;
        this.comment = "";
    }

    @Override
    public String getElementName()
    {
        return name;
    }

    @Override
    public String getElementComment()
    {
        return comment;
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> properties = new LinkedHashMap<>();

        properties.put("Name", getElementName());
        properties.put("Comment", getElementComment());

        return properties;
    }

    @Override
    public IScenarioElement getParent()
    {
        return parent;
    }

    @Override
    public IScenarioProject getScenarioProject()
    {
        IScenarioElement current = this;
        do {
            if (current instanceof IScenarioProject) {
                return (IScenarioProject) current;
            }
        } while ((current = current.getParent()) != null);
        return null;
    }

    @Override
    public int compareTo(IScenarioElement other)
    {
        return getElementName().compareTo(other.getElementName());
    }

    public void setParent(IScenarioElement parent)
    {
        this.parent = parent;
    }

    public void setElementName(String name)
    {
        this.name = name;
    }

    public void setElementComment(String comment)
    {
        this.comment = comment;
    }
}
