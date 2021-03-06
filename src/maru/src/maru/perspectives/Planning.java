package maru.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The planning perspective of the application. It is initially empty because
 * external user interface plugins are meant to contribute to this perspective.
 */
public class Planning implements IPerspectiveFactory
{
    public static final String ID = "maru.perspectives.Planning";

    @Override
    public void createInitialLayout(IPageLayout layout)
    {
        layout.setEditorAreaVisible(false);
    }
}
