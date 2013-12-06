package maru.core.internal.model;

import maru.core.model.IScenarioProject;

interface IScenarioProjectStorage
{
    static final String STORAGE_FILE_NAME = ".scenproject"; //$NON-NLS-1$

    IScenarioProject getScenarioProject();
    void writeScenarioProject();
}
