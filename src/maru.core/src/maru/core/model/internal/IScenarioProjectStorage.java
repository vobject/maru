package maru.core.model.internal;

import maru.core.model.IScenarioProject;

public interface IScenarioProjectStorage
{
    static final String STORAGE_FILE_NAME = ".scenproject"; //$NON-NLS-1$

    IScenarioProject getScenarioProject();
    void writeScenarioProject();
}
