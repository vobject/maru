package maru.centralbody.wizards;

import java.util.ArrayList;

import maru.IMaruResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.bodies.Earth;
import maru.centralbody.preferences.MapImagesEditor;
import maru.core.model.ICentralBody;

import org.orekit.errors.OrekitException;

/**
 * The central bodies supported by the wizard page.
 */
public enum CentralBodyFactory
{
    Earth;

    public ICentralBody createCentralBody(String imageName)
    {
        return createCentralBody(this.toString(), imageName);
    }

    public static ICentralBody createCentralBody(String name, String imageName)
    {
        switch (CentralBodyFactory.valueOf(name))
        {
            case Earth:
            {
                // try to interpret the image name as an external resource first
                IMaruResource res = MapImagesEditor.fromName(imageName);

                if (res == null) {
                    // try to get a bundle resource if it is no external resource
                    res = MaruCentralBodyResources.fromName(imageName);
                }

                try
                {
                    return new Earth(res);
                }
                catch (OrekitException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String[] getMapImages(String name)
    {
        ArrayList<String> images = new ArrayList<>();

        switch (CentralBodyFactory.valueOf(name))
        {
            case Earth:
            {
                // add predefined map images
                images.add(MaruCentralBodyResources.MAP_EARTH_1.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_2.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_3.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_4.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_5.getName());
                images.add(MaruCentralBodyResources.MAP_EARTH_6.getName());
            }
        }

        // FIXME: external map images are currently not assigned to a specific
        // central body of any sort. they are added to all available bodies.

        // add external map images to selection
        for (IMaruResource res : MapImagesEditor.getMapImageResources()) {
            images.add(res.getName());
        }

        return images.toArray(new String[images.size()]);
    }
}