package maru.report.views;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import maru.ui.MaruUIPlugin;
import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class ReportView extends ViewPart implements IUiProjectSelectionListener,
                                                    IUiProjectModelListener
{
    // TODO: remove this and initialize the report combo with the strings
    // from all available report controls.
    private static final String[] REPORT_TYPES = new String[] {
        "Propagation",
        "Visibility",
        "Eclipse"
    };

    private Text outputText;
    private Label selectedScenario;
    private Combo reportTypeCombo;
    private ReportTypeControl currentReportType;
    private Button startButton;
    private Button saveButton;

    @Override
    public void createPartControl(Composite parent)
    {
        Composite container = createContainerControl(parent);

        createOutputTextControl(container);
        createSettingsControl(container);

        MaruUIPlugin.getDefault().getUiModel().addUiProjectSelectionListener(this);
        MaruUIPlugin.getDefault().getUiModel().addUiProjectModelListener(this);
    }

    private Composite createContainerControl(Composite parent)
    {
        GridData containerData = new GridData();
        containerData.horizontalAlignment = GridData.FILL;
        containerData.verticalAlignment = GridData.FILL;
        containerData.grabExcessHorizontalSpace = true;
        containerData.grabExcessVerticalSpace = true;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(8, false));
        container.setLayoutData(containerData);

        return container;
    }

    private void createOutputTextControl(Composite container)
    {
        GridData outputTextData = new GridData();
        outputTextData.horizontalAlignment = GridData.FILL;
        outputTextData.verticalAlignment = GridData.FILL;
        outputTextData.grabExcessHorizontalSpace = true;
        outputTextData.grabExcessVerticalSpace = true;
        outputTextData.horizontalSpan = 4;

        outputText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        outputText.setLayoutData(outputTextData);

        final Font outputTextFont = new Font(outputText.getDisplay(), "Courier New", 9, SWT.NORMAL);
        outputText.setFont(outputTextFont);
        outputText.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e)
            {
                outputTextFont.dispose();
            }
        });
    }

    private void createSettingsControl(Composite container)
    {
        GridData settingsContainerData = new GridData();
        settingsContainerData.verticalAlignment = GridData.FILL;
        settingsContainerData.grabExcessVerticalSpace = true;
        settingsContainerData.horizontalSpan = 4;

        Composite settinsgContainer = new Composite(container, SWT.NONE);
        settinsgContainer.setLayout(new GridLayout(2, false));
        settinsgContainer.setLayoutData(settingsContainerData);

        createScenarioLabel(settinsgContainer);
        createReportTypeControl(settinsgContainer);

        // default report type
        currentReportType = new PropagationReportControl(settinsgContainer);
    }

    private void createScenarioLabel(Composite settinsgContainer)
    {
        GridData labelData = new GridData();
        labelData.horizontalAlignment = GridData.FILL;
        labelData.grabExcessHorizontalSpace = true;
        labelData.horizontalSpan = 2;

        selectedScenario = new Label(settinsgContainer, SWT.NONE);
        selectedScenario.setLayoutData(labelData);

        UiProject currentProject = UiModel.getDefault().getCurrentUiProject();
        if (currentProject != null) {
            selectedScenario.setText(currentProject.getName());
        } else {
            selectedScenario.setText("<no scenario>");
        }
    }

    private void createReportTypeControl(final Composite container)
    {
        GridData reportTypeData = new GridData();
        reportTypeData.horizontalAlignment = GridData.FILL;
        reportTypeData.grabExcessHorizontalSpace = true;
        reportTypeData.horizontalSpan = 2;

        reportTypeCombo = new Combo(container, SWT.READ_ONLY);
        reportTypeCombo.setLayoutData(reportTypeData);
        reportTypeCombo.setItems(REPORT_TYPES);
        reportTypeCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String selection = reportTypeCombo.getText();

                if (selection.equals("Propagation")) {
                    currentReportType.dispose();
                    currentReportType = new PropagationReportControl(container);
                } else if (selection.equals("Visibility")) {
                    currentReportType.dispose();
                    currentReportType = new VisibilityReportControl(container);
                } else if (selection.equals("Eclipse")) {
                    currentReportType.dispose();
                    currentReportType = new EclipseReportControl(container);
                } else {
                    throw new IllegalStateException();
                }
                // Add more report types here.

                createStartButtonControl(container);
                createSaveButtonControl(container);

                UiProject currentProject = UiModel.getDefault().getCurrentUiProject();
                if (currentProject != null) {
                    currentReportType.setCurrentProject(currentProject);
                    startButton.setEnabled(true);
                }

                outputText.setText("");
                container.getParent().layout(true, true);
            }
        });
    }

    private void createStartButtonControl(Composite container)
    {
        if (startButton != null) {
            startButton.dispose();
        }

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.END;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = 2;

        startButton = new Button(container, SWT.PUSH);
        startButton.setText("Create Report");
        startButton.setLayoutData(data);
        startButton.setEnabled(false);
        startButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                currentReportType.createReport(outputText);
            }
        });
    }

    private void createSaveButtonControl(Composite container)
    {
        if (saveButton != null) {
            saveButton.dispose();
        }

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.END;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = 2;

        saveButton = new Button(container, SWT.PUSH);
        saveButton.setText("Save Report");
        saveButton.setLayoutData(data);
        saveButton.setEnabled(true);
        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String[] filterNames = new String[] {"Text Files", "All Files (*.*)"};
                String[] filterExt = new String[] {"*.txt", "*.*"};

                FileDialog saveDlg = new FileDialog(getSite().getShell(), SWT.SAVE);
                saveDlg.setFilterNames(filterNames);
                saveDlg.setFilterExtensions(filterExt);
                saveDlg.setOverwrite(true);
                saveDlg.setFileName("report.txt");

                String savePath = saveDlg.open();
                if (savePath == null) {
                    return; // user aborted
                }

                try (PrintWriter saveFile = new PrintWriter(savePath)) {
                    saveFile.print(outputText.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setFocus()
    {
        reportTypeCombo.setFocus();
    }

    @Override
    public void dispose()
    {
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectModelListener(this);
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectSelectionListener(this);
        super.dispose();
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        currentReportType.setCurrentProject(project);
        selectedScenario.setText(project.getName());

        if (startButton != null) {
            startButton.setEnabled(true);
        }
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        currentReportType.setCurrentProject(project);
        selectedScenario.setText(project.getName());

        if (startButton != null) {
            startButton.setEnabled(true);
        }
    }

    @Override
    public void projectAdded(UiProject project)
    {

    }

    @Override
    public void projectChanged(UiProject project)
    {

    }

    @Override
    public void projectRemoved(UiProject project)
    {
        if (project == UiModel.getDefault().getCurrentUiProject())
        {
            currentReportType.setCurrentProject(null);
            currentReportType.disable();

            selectedScenario.setText("<no scenario>");
            startButton.setEnabled(false);
        }
    }
}
