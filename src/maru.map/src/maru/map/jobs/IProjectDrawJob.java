package maru.map.jobs;

import maru.ui.model.UiProject;

public interface IProjectDrawJob extends IDrawJob
{
    UiProject getProject();
    void setProject(UiProject project);
}
