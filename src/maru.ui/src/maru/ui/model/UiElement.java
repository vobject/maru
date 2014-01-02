package maru.ui.model;

import java.util.Map;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioElement;
import maru.ui.MaruUIPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionFilter;

public abstract class UiElement implements IActionFilter
{
    protected enum UiSortPriority
    {
        DEFAULT,
        CENTRALBODY,
        GROUNDSTATIONS,
        SATELLITES,
        TIMEPOINTS
    }

    private UiSortPriority sortPriority;
    private final UiParent parent;
    private IScenarioElement underlying;

    public UiElement(UiParent parent, IScenarioElement underlying)
    {
        this.parent = parent;
        this.underlying = underlying;
    }

    public String getName()
    {
        return getUnderlyingElement().getElementName();
    }

    public String getComment()
    {
        return getUnderlyingElement().getElementComment();
    }

    public ImageDescriptor getImageDescriptor()
    {
        return MaruUIPlugin.getImageDescriptor(getImagePath());
    }

    public UiParent getUiParent()
    {
        return parent;
    }

    public UiProject getUiProject()
    {
        return parent.getUiProject();
    }

    public UiSortPriority getSortPriority()
    {
        return sortPriority;
    }

    public IScenarioElement getUnderlyingElement()
    {
        return underlying;
    }

    public Map<String, String> getPropertyMap()
    {
        return underlying.getPropertyMap();
    }

    public void setUnderlyingElement(IScenarioElement element)
    {
        underlying = element;
    }

    public void setName(String name)
    {
        CoreModel model = CoreModel.getDefault();
        model.renameElement(getUnderlyingElement(), name, true);
    }

    public void setComment(String comment)
    {
        CoreModel model = CoreModel.getDefault();
        model.commentElement(getUnderlyingElement(), comment, true);
    }

    protected void setSortPriority(UiSortPriority priority)
    {
        sortPriority = priority;
    }

    protected String getImagePath()
    {
        return "icons/sample.gif";
    }

    @Override
    public boolean testAttribute(Object target, String name, String value)
    {
        if (name.equals("instanceof.underlying"))
        {
            // this is usually invoked by a property page filter to test
            // whether its property page should be displayed or not

            try
            {
                Class<?> classToCheckFor = Class.forName(value);
                Class<?> underlyingClass = getUnderlyingElement().getClass();

                return underlyingClass.isAssignableFrom(classToCheckFor);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }
}
