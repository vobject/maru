package maru.core.workspace.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import maru.core.model.ICentralBody;
import maru.core.model.internal.IScenarioModel;
import maru.core.model.internal.IScenarioProjectStorage;
import maru.core.model.internal.ScenarioProject;
import maru.core.model.internal.Timepoint;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class ScenarioProjectStorage implements IScenarioProjectStorage
{
    private final IProject project;
    private ScenarioProject scenario;

    public ScenarioProjectStorage(IScenarioModel model,
                                  IProject project,
                                  Timepoint start,
                                  Timepoint stop,
                                  String comment,
                                  ICentralBody centralBody)
    {
        this.project = project;

        scenario = new ScenarioProject(project.getName(), start, stop, comment, centralBody);
        scenario.setParent(model);
    }

    public ScenarioProjectStorage(IScenarioModel model, IProject project)
    {
        this.project = project;

        IFile storeFile = getStoreFileResource(project);

        try
        {
            InputStream inStream = storeFile.getContents();
            ObjectInputStream objInput = new ObjectInputStream(inStream);

            scenario = (ScenarioProject) objInput.readObject();
            inStream.close();

            scenario.setParent(model);
        }
        catch (IOException | ClassNotFoundException | CoreException e)
        {
            e.printStackTrace();
        }
    }

    public IProject getProject()
    {
        return project;
    }

    @Override
    public ScenarioProject getScenarioProject()
    {
        return scenario;
    }

    @Override
    public void writeScenarioProject()
    {
        save();
    }

    public boolean exists()
    {
        return getStoreFileResource(project).exists();
    }

    public void save()
    {
        try
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutput = new ObjectOutputStream(outStream);

            objOutput.writeObject(scenario);
            objOutput.close();

            IFile storeFile = getStoreFileResource(project);
            InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());

            if (!storeFile.exists()) {
                storeFile.create(inStream, true, null);
            } else {
                storeFile.setContents(inStream, true, false, null);
            }
        }
        catch (IOException | CoreException e)
        {
            e.printStackTrace();
        }
    }

    protected IFile getStoreFileResource(IProject project)
    {
        return project.getFile(STORAGE_FILE_NAME);
    }
}
