package maru.ui.popups.actionprovider;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.actions.LTKLauncher;
import org.eclipse.ui.progress.WorkbenchJob;

/**
 * This class is based on org.eclipse.ui.actions.DeleteResourceAction.
 */
@SuppressWarnings("restriction")
public class ScenarioProjectDeleteAction extends SelectionListenerAction
{
    /** The id of this action. */
    public static final String ID = PlatformUI.PLUGIN_ID + ".DeleteResourceAction";

    private final IShellProvider shellProvider;

    public ScenarioProjectDeleteAction(IShellProvider provider)
    {
        super("&Delete");
        Assert.isNotNull(provider);

        setId(ID);
        this.shellProvider = provider;
    }

    /**
     * The <code>DeleteResourceAction</code> implementation of this
     * <code>SelectionListenerAction</code> method disables the action if the
     * selection contains phantom resources or non-resources
     */
    @Override
    protected boolean updateSelection(IStructuredSelection selection)
    {
        return super.updateSelection(selection) && canDelete(getSelectedResourcesArray());
    }

    /*
     * (non-Javadoc) Method declared on IAction.
     */
    @Override
    public void run()
    {
        final IResource[] resources = getSelectedResourcesArray();

        if (LTKLauncher.openDeleteWizard(getStructuredSelection())) {
            return;
        }

        Job deletionCheckJob = new Job(IDEWorkbenchMessages.DeleteResourceAction_checkJobName) {
            /*
             * (non-Javadoc)
             * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
             */
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
                if (resources.length == 0) {
                    return Status.CANCEL_STATUS;
                }
                scheduleDeleteJob(resources);
                return Status.OK_STATUS;
            }

            /*
             * (non-Javadoc)
             * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
             */
            @Override
            public boolean belongsTo(Object family)
            {
                if (IDEWorkbenchMessages.DeleteResourceAction_jobName.equals(family)) {
                    return true;
                }
                return super.belongsTo(family);
            }
        };

        deletionCheckJob.schedule();
    }

    /**
     * Schedule a job to delete the resources to delete.
     */
    private void scheduleDeleteJob(final IResource[] resourcesToDelete)
    {
        // use a non-workspace job with a runnable inside so we can avoid periodic updates
        Job deleteJob = new Job(IDEWorkbenchMessages.DeleteResourceAction_jobName) {
            @Override
            public IStatus run(final IProgressMonitor monitor)
            {
                try
                {
                    final DeleteResourcesOperation op = new DeleteResourcesOperation(resourcesToDelete, IDEWorkbenchMessages.DeleteResourceAction_operationLabel, true);

                    // If we are deleting projects, do not
                    // execute the operation in the undo history, since it cannot be
                    // properly restored.  Just execute it directly so it won't be
                    // added to the undo history.
                    if (!containsOnlyProjects(resourcesToDelete)) {
                        return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, monitor, WorkspaceUndoUtil.getUIInfoAdapter(shellProvider.getShell()));
                    }

                    // We must compute the execution status first so that any user prompting
                    // or validation checking occurs.  Do it in a syncExec because
                    // we are calling this from a Job.
                    WorkbenchJob statusJob = new WorkbenchJob("Status checking") {
                        /* (non-Javadoc)
                         * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
                         */
                        @Override
                        public IStatus runInUIThread(IProgressMonitor monitor) {
                            return op.computeExecutionStatus(monitor);
                        }
                    };

                    statusJob.setSystem(true);
                    statusJob.schedule();
                    try {//block until the status is ready
                        statusJob.join();
                    } catch (InterruptedException e) {
                        //Do nothing as status will be a cancel
                    }

                    if (statusJob.getResult().isOK()) {
                        return op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(shellProvider.getShell()));
                    }
                    return statusJob.getResult();
                }
                catch (ExecutionException e)
                {
                    if (e.getCause() instanceof CoreException) {
                        return ((CoreException)e.getCause()).getStatus();
                    }
                    return new Status(IStatus.ERROR, IDEWorkbenchPlugin.IDE_WORKBENCH, e.getMessage(),e);
                }
            }

            /*
             * (non-Javadoc)
             * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
             */
            @Override
            public boolean belongsTo(Object family)
            {
                if (IDEWorkbenchMessages.DeleteResourceAction_jobName.equals(family)) {
                    return true;
                }
                return super.belongsTo(family);
            }

        };
        deleteJob.setUser(true);
        deleteJob.schedule();
    }

    /**
     * Returns whether delete can be performed on the current selection.
     *
     * @param resources the selected resources
     * @return <code>true</code> if the resources can be deleted, and
     *         <code>false</code> if the selection contains non-resources or
     *         phantom resources
     */
    private boolean canDelete(IResource[] resources)
    {
        // allow only projects or only non-projects to be selected;
        // note that the selection may contain multiple types of resource
        if (!(containsOnlyProjects(resources) || containsOnlyNonProjects(resources))) {
            return false;
        }

        if (resources.length == 0) {
            return false;
        }

        // Return true if everything in the selection exists.
        for (IResource resource : resources)
        {
            if (resource.isPhantom()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the selection contains only non-projects.
     *
     * @param resources the selected resources
     * @return <code>true</code> if the resources contains only non-projects,
     *         and <code>false</code> otherwise
     */
    private boolean containsOnlyNonProjects(IResource[] resources)
    {
        int types = getSelectedResourceTypes(resources);
        // check for empty selection
        if (types == 0) {
            return false;
        }
        // note that the selection may contain multiple types of resource
        return (types & IResource.PROJECT) == 0;
    }

    /**
     * Returns whether the selection contains only projects.
     *
     * @param resources the selected resources
     * @return <code>true</code> if the resources contains only projects, and
     *         <code>false</code> otherwise
     */
    private boolean containsOnlyProjects(IResource[] resources)
    {
        int types = getSelectedResourceTypes(resources);
        // note that the selection may contain multiple types of resource
        return types == IResource.PROJECT;
    }

    /**
     * Return an array of the currently selected resources.
     *
     * @return the selected resources
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private IResource[] getSelectedResourcesArray()
    {
        List selection = getSelectedResources();
        IResource[] resources = new IResource[selection.size()];
        selection.toArray(resources);
        return resources;
    }

    /**
     * Returns a bit-mask containing the types of resources in the selection.
     *
     * @param resources the selected resources
     */
    private int getSelectedResourceTypes(IResource[] resources)
    {
        int types = 0;
        for (IResource resource : resources)
        {
            types |= resource.getType();
        }
        return types;
    }
}
