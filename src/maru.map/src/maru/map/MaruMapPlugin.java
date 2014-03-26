package maru.map;

import java.util.ArrayList;
import java.util.List;

import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.settings.scenario.ScenarioModelSettings;
import maru.map.views.gl.IGLDrawJobRunner;
import maru.map.views.gl.ITextureListener;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.jogamp.opengl.util.texture.TextureData;

/**
 * The activator class controls the plug-in life cycle
 */
public class MaruMapPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.map"; //$NON-NLS-1$

    private static MaruMapPlugin plugin;

    private ScenarioModelSettings settings;
    private final List<IGLDrawJobRunner> glDrawJobRunners = new ArrayList<>();
    private final List<ITextureListener> textureListeners = new ArrayList<>();

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        settings = new ScenarioModelSettings(getPreferenceNode());
        settings.attachToModel();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        settings.detachFromModel();
        settings.save();

        plugin = null;
        super.stop(context);
    }

    public static MaruMapPlugin getDefault()
    {
        return plugin;
    }

    public ScenarioModelSettings getScenarioModelSettings()
    {
        return settings;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static Image getImage(String imagePath)
    {
        return getImageDescriptor(imagePath).createImage();
    }

    public void addGLDrawJobRunner(IGLDrawJobRunner runner)
    {
        if (!glDrawJobRunners.contains(runner)) {
            glDrawJobRunners.add(runner);
        }
    }

    public void removeGLDrawJobRunner(IGLDrawJobRunner runner)
    {
        if (glDrawJobRunners.contains(runner)) {
            glDrawJobRunners.remove(runner);
        }
    }

    public void registerProjectDrawJob(GLProjectDrawJob job)
    {
        for (IGLDrawJobRunner jobRunner : glDrawJobRunners) {
            jobRunner.addProjectDrawJob(job);
        }
    }

    public void registerProjectAnimationJob(GLProjectAnimationJob job)
    {
        for (IGLDrawJobRunner jobRunner : glDrawJobRunners) {
            jobRunner.addProjectAnimationJob(job);
        }
    }

    public void redraw()
    {
        for (IGLDrawJobRunner jobRunner : glDrawJobRunners) {
            jobRunner.redraw();
        }
    }

    public void addTextureListener(ITextureListener listener)
    {
        if (!textureListeners.contains(listener)) {
            textureListeners.add(listener);
        }
    }

    public void removeTextureListener(ITextureListener listener)
    {
        if (textureListeners.contains(listener)) {
            textureListeners.remove(listener);
        }
    }

    public boolean hasTextureListeners()
    {
        return !textureListeners.isEmpty();
    }

    public void notifyTextureListeners(TextureData data)
    {
        textureListeners.forEach(listener -> listener.textureUpdated(data));
    }

    private IEclipsePreferences getPreferenceNode()
    {
        return InstanceScope.INSTANCE.getNode(PLUGIN_ID);
    }
}
