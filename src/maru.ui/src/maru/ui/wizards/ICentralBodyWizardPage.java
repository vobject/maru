package maru.ui.wizards;

import maru.core.model.ICentralBody;

import org.eclipse.jface.wizard.IWizardPage;

public interface ICentralBodyWizardPage extends IWizardPage
{
    ICentralBody createCentralBody();
}
