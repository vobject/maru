package maru.ui.debug.handlers;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

public class OpenThesisHandler extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        String DROPBOX_PUBLIC_THESIS_PDF = "https://dl.dropboxusercontent.com/u/6632736/thesis.pdf";
        String LOCAL_THESIS_PATH = System.getProperty("java.io.tmpdir") + "/thesis.pdf";

        try
        {
            URL url = new URL(DROPBOX_PUBLIC_THESIS_PDF);
            DataInputStream inStream = new DataInputStream(url.openStream());
            FileOutputStream outStream = new FileOutputStream(LOCAL_THESIS_PATH, false);

            int read = 0;
            byte[] bytes = new byte[4096];

            while ((read = inStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, read);
            }

            inStream.close();
            outStream.close();

            Program.launch(LOCAL_THESIS_PATH);
        }
        catch (Exception e)
        {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), e.getMessage(), "Fail!");
            e.printStackTrace();
        }

        return null;
    }
}
