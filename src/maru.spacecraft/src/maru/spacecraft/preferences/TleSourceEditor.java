package maru.spacecraft.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import maru.spacecraft.utils.TleUtils;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class TleSourceEditor extends ListEditor
{
    public TleSourceEditor(String name, String labelText, Composite parent)
    {
        super(name, labelText, parent);
    }

    @Override
    protected String createList(String[] items)
    {
        StringBuilder path = new StringBuilder(""); //$NON-NLS-1$

        for (String item : items)
        {
            path.append(item);
            path.append(TleUtils.TLE_SOURCE_SEPERATOR);
        }
        return path.toString();
    }

    @Override
    protected String getNewInputObject()
    {
        InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
                                          "New external TLE Source",
                                          "TLE Source",
                                          "Enter local path or URL",
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
        List<String> elements = parsePreferenceString(stringList);
        return elements.toArray(new String[elements.size()]);
    }

    public static List<String> parsePreferenceString(String stringList)
    {
        StringTokenizer tokenizer = new StringTokenizer(stringList, TleUtils.TLE_SOURCE_SEPERATOR);
        ArrayList<String> elements = new ArrayList<>();

        while (tokenizer.hasMoreElements()) {
            elements.add(tokenizer.nextToken());
        }
        return elements;
    }
}
