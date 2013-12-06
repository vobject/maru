package maru.report.views;

import maru.ui.model.UiProject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public abstract class ReportTypeControl
{
    private final String reportName;
    private UiProject currentProject;

    public ReportTypeControl(String name, Composite parentControl)
    {
        createPartControl(parentControl);
        this.reportName = name;
    }

    public abstract void dispose();

    public abstract void enable();
    public abstract void disable();

    public String getReportName()
    {
        return reportName;
    }

    public UiProject getCurrentProject()
    {
        return currentProject;
    }

    public void setCurrentProject(UiProject project)
    {
        currentProject = project;
    }

    public abstract void createReport(Text ouput);

    protected abstract void createPartControl(Composite parent);
    protected abstract Composite getContainer();
}
