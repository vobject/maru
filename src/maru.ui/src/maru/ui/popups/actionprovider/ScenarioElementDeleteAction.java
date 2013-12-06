package maru.ui.popups.actionprovider;

import maru.core.model.CoreModel;
import maru.ui.model.UiGroundstation;
import maru.ui.model.UiSpacecraft;
import maru.ui.model.UiTimepoint;

import org.eclipse.ui.actions.SelectionListenerAction;

public class ScenarioElementDeleteAction extends SelectionListenerAction
{
    public ScenarioElementDeleteAction()
    {
        super("&Delete");
    }

    @Override
    public void run()
    {
        // iterate over each selected element and remove it from the model
        // and update the ui because the model changed

        for (Object element : getStructuredSelection().toList())
        {
            if (element instanceof UiGroundstation)
            {
                UiGroundstation groundstation = (UiGroundstation) element;
                CoreModel.getDefault().removeElement(groundstation.getUnderlyingElement(), true);
            }
            else if (element instanceof UiSpacecraft)
            {
                UiSpacecraft spacecraft = (UiSpacecraft) element;
                CoreModel.getDefault().removeElement(spacecraft.getUnderlyingElement(), true);
            }
            else if (element instanceof UiTimepoint)
            {
                UiTimepoint timepoint = (UiTimepoint) element;
                CoreModel.getDefault().removeTimepoint(timepoint.getUnderlyingElement(), true);
            }
        }
    }
}
