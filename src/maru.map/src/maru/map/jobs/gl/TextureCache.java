package maru.map.jobs.gl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

import maru.IMaruResource;
import maru.core.utils.PathUtil;
import maru.map.MaruMapResources;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureCache
{
    private final GL2 gl;
    private final Map<String, Texture> textures;

    public TextureCache(GL2 gl)
    {
        this.gl = gl;
        this.textures = new HashMap<>();
    }

    public void dispose()
    {
        clear();
    }

    public Texture get(MaruMapResources res)
    {
        return get(res.getBundlePath());
    }

    public Texture get(String path)
    {
        if (textures.containsKey(path)) {
            return textures.get(path);
        } else {
            return load(path);
        }
    }

    /**
     * Pre-load or replace a specific texture.
     *
     * @param res The texture resource to load.
     * @return The loaded texture if successful or null if texture could not be loaded.
     */
    public Texture load(IMaruResource res)
    {
        return load(res.getPath());
    }

    /**
     * Pre-load or replace a specific texture.
     *
     * @param path Path of the texture to load.
     * @return The loaded texture if successful. Throws runtime exception if texture could not be loaded.
     */
    public Texture load(String path)
    {
        if (textures.containsKey(path)) {
            // dispose the texture previously save under that id.
            textures.get(path).destroy(gl);
        }

        try
        {
            Texture texture = loadImage(path);
            textures.put(path, texture);
            return texture;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Unable to load texture " + path);
        }
    }

    /**
     * Clears the texture cache, freeing all loaded textures.
     */
    public void clear()
    {
        for (Texture texture : textures.values()) {
            texture.destroy(gl);
        }
        textures.clear();
    }

    private static Texture loadImage(String path) throws GLException, IOException
    {
        URL url = PathUtil.getUrlFromPath(path);
        return TextureIO.newTexture(url.openStream(), true, PathUtil.getSuffixFromPath(path));
    }
}
