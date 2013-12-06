package maru.ui.views.properties;

import java.util.Map;

import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ITimepoint;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;
import maru.ui.views.ScenarioModelViewPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Properties extends ScenarioModelViewPart
{
    private Table propertiesTable;
    private IScenarioElement currentElement;

    @Override
    public void createPartControl(Composite parent)
    {
        parent.setLayout(new FillLayout());
        propertiesTable = new Table(parent, SWT.FULL_SELECTION);
        propertiesTable.setHeaderVisible(true);
        propertiesTable.setLinesVisible(true);

        (new TableColumn(propertiesTable, SWT.NONE)).setText("Property");
        (new TableColumn(propertiesTable, SWT.NONE)).setText("Value");

        propertiesTable.getColumn(0).pack();
        propertiesTable.getColumn(1).pack();

        UiModel.getDefault().addUiProjectSelectionListener(this);
    }

    @Override
    public void setFocus()
    {
        propertiesTable.setFocus();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        UiModel.getDefault().removeUiProjectSelectionListener(this);
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        activeElementChanged(project, element);
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        currentElement = element.getUnderlyingElement();
        refreshTableContent();

        propertiesTable.getColumn(0).pack();
        propertiesTable.getColumn(1).pack();
    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {
        if (element == currentElement) {
            currentElement = null;
            propertiesTable.removeAll();
        }
    }

    @Override
    public void elementRenamed(IScenarioElement element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    @Override
    public void elementCommented(IScenarioElement element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    @Override
    public void propagatablesTimeChanged(IScenarioProject element)
    {
        refreshTableContent();
    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    @Override
    public void timepointChanged(ITimepoint element)
    {
        if (element == currentElement) {
            refreshTableContent();
        }
    }

    private void refreshTableContent()
    {
        if (currentElement == null) {
            return;
        }

        propertiesTable.setRedraw(false);
        propertiesTable.removeAll();

        Map<String, String> propertyMap = currentElement.getPropertyMap();
        for (Map.Entry<String, String> property : propertyMap.entrySet())
        {
            TableItem item = new TableItem(propertiesTable, SWT.NONE);
            item.setText(0, property.getKey());
            item.setText(1, property.getValue());
        }
        propertiesTable.setRedraw(true);
    }
}
