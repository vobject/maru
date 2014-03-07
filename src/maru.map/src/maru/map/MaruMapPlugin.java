package maru.map;

import java.util.ArrayList;
import java.util.List;

import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.settings.uiproject.UiProjectsSettings;
import maru.map.views.gl.IGLDrawJobRunner;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class MaruMapPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.map"; //$NON-NLS-1$

    private static MaruMapPlugin plugin;

    private UiProjectsSettings uiProjectsSettings;
    private final List<IGLDrawJobRunner> glDrawJobRunners = new ArrayList<>();

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);

        plugin = this;
        uiProjectsSettings = new UiProjectsSettings(getUiProjectsPreferenceNode());
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        plugin.getPreferenceNode().flush();
        plugin = null;

        super.stop(context);
    }

    public static MaruMapPlugin getDefault()
    {
        return plugin;
    }

    public IEclipsePreferences getPreferenceNode()
    {
        return InstanceScope.INSTANCE.getNode(PLUGIN_ID);
    }

    public Preferences getUiProjectsPreferenceNode()
    {
        return getPreferenceNode().node("UiProjects");
    }

    public UiProjectsSettings getUiProjectsSettings()
    {
        return uiProjectsSettings;
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
}
