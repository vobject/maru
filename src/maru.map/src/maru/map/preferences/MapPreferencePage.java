package maru.map.preferences;

import maru.map.MaruMapPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MapPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public MapPreferencePage()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruMapPlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors()
    {
        addField(new BooleanFieldEditor(MapPreferenceConstants.P_MAP_ANTI_ALIASING,
                                        "Enable Anti-aliasing for ground tracks",
                                        getFieldEditorParent()));

        addField(new BooleanFieldEditor(MapPreferenceConstants.P_MAP_OUTLINE_ICONS,
                                        "Display icons on the map with a black outline",
                                        getFieldEditorParent()));

        addField(new BooleanFieldEditor(MapPreferenceConstants.P_MAP_OUTLINE_TEXT,
                                        "Display text on the map with a black outline",
                                        getFieldEditorParent()));

        ComboFieldEditor font =
            new ComboFieldEditor(MapPreferenceConstants.P_MAP_FONT,
                                "Map text font",
                                getFontNames(),
                                getFieldEditorParent());
        addField(font);

        IntegerFieldEditor fontSize =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_FONT_SIZE,
                                   "Map text font size",
                                   getFieldEditorParent());
        fontSize.setValidRange(8, 96);
        addField(fontSize);

        IntegerFieldEditor groundtrackStepSize =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE,
                                   "Ground track step size in seconds",
                                   getFieldEditorParent());
        groundtrackStepSize.setValidRange(10, 1200);
        addField(groundtrackStepSize);

        IntegerFieldEditor visibilityCirclesPoints =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS,
                                   "Number of points to use for visibility circles",
                                   getFieldEditorParent());
        visibilityCirclesPoints.setValidRange(8, 360);
        addField(visibilityCirclesPoints);

        IntegerFieldEditor latLonLineStepSize =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE,
                                   "Latitude/Longitude lines step size in degree",
                                   getFieldEditorParent());
        latLonLineStepSize.setValidRange(10, 90);
        addField(latLonLineStepSize);

        IntegerFieldEditor nightOverlayOpacity =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_OPACITY,
                                   "Night overlay opacity in percent",
                                   getFieldEditorParent());
        nightOverlayOpacity.setValidRange(0, 100);
        addField(nightOverlayOpacity);

        IntegerFieldEditor nightOverlayPixelSteps =
            new IntegerFieldEditor(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_PIXELSTEPS,
                                   "Night overlay step size in pixels",
                                   getFieldEditorParent());
        nightOverlayPixelSteps.setValidRange(1, 32);
        addField(nightOverlayPixelSteps);
    }

    private String[][] getFontNames()
    {
        String[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String[][] fontList = new String[fonts.length][2];

        for (int i = 0; i < fonts.length; i++)
        {
            fontList[i][0] = fonts[i];
            fontList[i][1] = fonts[i];
        }
        return fontList;
    }
}
