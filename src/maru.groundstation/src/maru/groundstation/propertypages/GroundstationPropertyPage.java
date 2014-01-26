package maru.groundstation.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioProject;
import maru.groundstation.Groundstation;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.controls.GroundstationControls;
import maru.ui.propertypages.UiVisiblePropertyPage;

import org.eclipse.swt.widgets.Composite;
import org.orekit.bodies.GeodeticPoint;

public class GroundstationPropertyPage extends UiVisiblePropertyPage
{
    private GroundstationControls gsControls;

    @Override
    protected String[] getImageNames()
    {
        // return an empty array be default
        return new String[] {
            "", // empty string allows to disable element image
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_1.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_2.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_3.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_4.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_5.getName(),
        };
    }

    @Override
    protected IMaruResource getImageFromName(String name)
    {
        return MaruGroundstationResources.fromName(name);
    }

    @Override
    public Groundstation getScenarioElement()
    {
        return (Groundstation) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        if (gsControls.isValid()) {
            setErrorMessage(null);
        } else {
            setErrorMessage(gsControls.getErrorMessage());
            return false;
        }

        if (gsControls.isModified())
        {
            Groundstation element = getScenarioElement();
            IScenarioProject scenario = element.getScenarioProject();

            GeodeticPoint newPosition = gsControls.getGeodeticPoint();
            double newElevation = gsControls.getElevationAngle();
            CoreModel.getDefault().changeInitialCoordinate(element, newPosition, newElevation, true);

            gsControls.refreshDefaults(scenario, element);
        }

        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        Composite container = super.createContents(parent);
        addSeparator(container);

        Groundstation element = getScenarioElement();
        IScenarioProject scenario = element.getScenarioProject();

        gsControls = createGroundstationControls(container, scenario, element);

        return container;
    }

    private GroundstationControls createGroundstationControls(Composite container,
                                                              IScenarioProject scenario,
                                                              IGroundstation element)
    {
        return new GroundstationControls(container, scenario, element);
    }
}
