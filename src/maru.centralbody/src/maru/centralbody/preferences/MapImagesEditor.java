package maru.centralbody.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import maru.IMaruResource;
import maru.MaruResource;
import maru.centralbody.MaruCentralBodyPlugin;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class MapImagesEditor extends ListEditor
{
    public MapImagesEditor(String name, String labelText, Composite parent)
    {
        super(name, labelText, parent);
    }

    @Override
    protected String createList(String[] items)
    {
        StringBuilder path = new StringBuilder("");

        for (String item : items)
        {
            path.append(item);
            path.append(";");
        }
        return path.toString();
    }

    @Override
    protected String getNewInputObject()
    {
        InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
                                          "New external map image",
                                          "Map image",
                                          "Enter local file path",
                                          null);
        if (dlg.open() == Window.OK) {
            return dlg.getValue();
        } else {
            return null;
        }
    }

    @Override
    protected String[] parseString(String stringList)
    {
        Collection<String> elements = parsePreferenceString(stringList);
        return elements.toArray(new String[elements.size()]);
    }

    public static Collection<IMaruResource> getMapImageResources()
    {
        IPreferenceStore store = MaruCentralBodyPlugin.getDefault().getPreferenceStore();
        String stringList = store.getString(PreferenceConstants.P_EXTERNAL_IMAGES);

        Collection<IMaruResource> images = new ArrayList<>();
        for (String imgPath : parsePreferenceString(stringList))
        {
            // for external image resources, name and path is the same
            images.add(new MaruResource(imgPath, imgPath));
        }
        return images;
    }

    public static IMaruResource fromName(String name)
    {
        for (IMaruResource imgRes : getMapImageResources())
        {
            if (imgRes.getName().equals(name)) {
                return imgRes;
            }
        }
        return null;
    }

    public static IMaruResource fromPath(String path)
    {
        for (IMaruResource imgRes : getMapImageResources())
        {
            if (imgRes.getPath().equals(path)) {
                return imgRes;
            }
        }
        return null;
    }

    public static Collection<String> parsePreferenceString(String stringList)
    {
        StringTokenizer tokenizer = new StringTokenizer(stringList, ";");
        ArrayList<String> elements = new ArrayList<>();

        while (tokenizer.hasMoreElements()) {
            elements.add(tokenizer.nextToken());
        }
        return elements;
    }
}
