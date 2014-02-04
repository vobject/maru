package maru.ui.wizards;

import maru.core.model.ICentralBody;

import org.eclipse.jface.wizard.IWizardPage;
import org.orekit.errors.OrekitException;

public interface ICentralBodyWizardPage extends IWizardPage
{
    ICentralBody createCentralBody() throws OrekitException;
}
