package maru.ui.navigators.scenario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maru.core.model.CoreModel;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;
import maru.core.model.IVisibleElement;
import maru.core.model.ScenarioModelAdapter;
import maru.core.workspace.WorkspaceModel;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.IUiProjectSelectionProvider;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiParent;
import maru.ui.model.UiProject;
import maru.ui.model.internal.UiProjectModelManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

public class ContentProvider extends BaseWorkbenchContentProvider
                             implements IUiProjectSelectionProvider
{
    class CurrentSelectionListener implements ISelectionListener
    {
        private UiProject currentProject;

        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection)
        {
            if (!(part instanceof CommonNavigator)) {
                // we are currently only listening to selections
                // inside the ScenarioExplorer
                return;
            }

            ITreeSelection treeSelection = (ITreeSelection)selection;
            Iterator<?> iterator = treeSelection.iterator();

            if (!iterator.hasNext()) {
                return;
            }
            Object object = iterator.next();

            if (object instanceof IProject)
            {
                IProject project = (IProject) object;

                WorkspaceModel workspaceModel = WorkspaceModel.getDefault();
                IScenarioProject scenarioProject = workspaceModel.getProject(project);

                UiModel uiModel = UiModel.getDefault();
                UiProject activeUiProject = uiModel.getUiProject(scenarioProject);

                if (activeUiProject == currentProject) {
                    // we reselected the current project - no update needed
                    return;
                }

                currentProject = activeUiProject;
                notifyProjectSelectionChanged(activeUiProject, activeUiProject);
            }
            else if (object instanceof UiElement)
            {
                UiElement element = (UiElement) object;

                if (element.getUiProject() == currentProject)
                {
                    // an element in the current project was selected
                    notifyElementSelectionChanged(element);
                }
                else
                {
                    // we selected an element in another project
                    currentProject = element.getUiProject();
                    notifyProjectSelectionChanged(element.getUiProject(), element);
                }
            }
        }
    }

    class ScenarioModelListener extends ScenarioModelAdapter
    {
        @Override
        public void scenarioCreated(IScenarioProject project)
        {
            refreshViewer(project, true);
        }

        @Override
        public void scenarioAdded(IScenarioProject project)
        {
            refreshViewer(project, true);
        }

        @Override
        public void scenarioRemoved(IScenarioProject project)
        {
            refreshViewer(project, false);
        }

        @Override
        public void elementAdded(IScenarioElement element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementRemoved(IScenarioElement element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementRenamed(IScenarioElement element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementColorChanged(IVisibleElement element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementImageChanged(IVisibleElement element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementInitialCoordinateChanged(IGroundstation element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void elementInitialCoordinateChanged(ISpacecraft element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void timepointStartChanged(ITimepoint element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void timepointStopChanged(ITimepoint element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void timepointAdded(ITimepoint element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void timepointRemoved(ITimepoint element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }

        @Override
        public void timepointChanged(ITimepoint element)
        {
            refreshViewer(element.getScenarioProject(), false);
        }
    }

    protected CommonViewer viewer;
    protected Object input;

    protected ScenarioModelListener scenarioModelListener;

    private final List<IUiProjectSelectionListener> selectionListeners;

    public ContentProvider()
    {
        selectionListeners = new ArrayList<>();

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        ISelectionService service = window.getSelectionService();
        service.addSelectionListener(new CurrentSelectionListener());

        scenarioModelListener = new ScenarioModelListener();

        addProjectSelectionListener(UiProjectModelManager.getDefault());
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
        super.inputChanged(viewer, oldInput, newInput);

        this.viewer = (CommonViewer) viewer;
        this.viewer.setSorter(new ContentSorter());
        this.input = newInput;

        if (oldInput == null && newInput != null) {
            CoreModel.getDefault().addScenarioModelListener(scenarioModelListener);
        } else if (oldInput != null && newInput == null) {
            CoreModel.getDefault().removeScenarioModelListener(scenarioModelListener);
        }
    }

    @Override
    public Object[] getChildren(Object element)
    {
        if (element instanceof IProject) {
            return getChildren((IProject) element);
        } else if (element instanceof UiParent) {
            return getChildren((UiParent) element);
        } else {
            return super.getChildren(element);
        }
    }

    protected Object[] getChildren(IProject project)
    {
        if (!project.isOpen()) {
            return super.getChildren(project);
        }

        WorkspaceModel workspaceModel = WorkspaceModel.getDefault();
        IScenarioProject scenario = workspaceModel.getProject(project);

        UiModel uiModel = UiModel.getDefault();
        UiProject uiProject = uiModel.getUiProject(scenario);
        return uiProject.getChildren().toArray();
    }

    protected Object[] getChildren(UiParent parent)
    {
        return parent.getChildren().toArray();
    }

    @Override
    public boolean hasChildren(Object element)
    {
        return (getChildren(element).length > 0);
    }

    @Override
    public void dispose()
    {
        removeProjectSelectionListener(UiProjectModelManager.getDefault());
        CoreModel.getDefault().removeScenarioModelListener(scenarioModelListener);
        super.dispose();
    }

    private void refreshViewer(final IScenarioProject scenario, final boolean expand)
    {
        Display.getDefault().asyncExec(new Runnable()
        {
            @Override
            public void run()
            {
                if (viewer.getControl().isDisposed()) {
                    return;
                }
                viewer.refresh();

                if (expand) {
                    IProject project = WorkspaceModel.getDefault().getProject(scenario);
                    viewer.expandToLevel(project, 1);
                }
            }
        });
    }

    @Override
    public void addProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }

    @Override
    public void removeProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        if (selectionListeners.contains(listener)) {
            selectionListeners.remove(listener);
        }
    }

    private void notifyProjectSelectionChanged(UiProject activeProject, UiElement selectedElement)
    {
        for (IUiProjectSelectionListener listener : selectionListeners) {
            listener.activeProjectChanged(activeProject, selectedElement);
        }
    }

    private void notifyElementSelectionChanged(UiElement selectedElement)
    {
        for (IUiProjectSelectionListener listener : selectionListeners) {
            listener.activeElementChanged(selectedElement.getUiProject(), selectedElement);
        }
    }
}
