package maru.map.jobs.gl;

import javax.media.opengl.GL;

import maru.map.jobs.DrawJob;
import maru.map.views.gl.TextureCache;

import com.sun.opengl.util.j2d.TextRenderer;

public abstract class GLDrawJob extends DrawJob
{
    private GL gl;
    private TextRenderer text;
    private TextureCache textureCache;

    public GL getGL()
    {
        return gl;
    }

    public void setGL(GL gl)
    {
        this.gl = gl;
    }

    public TextRenderer getTextRenderer()
    {
        return text;
    }

    public void setTextRenderer(TextRenderer text)
    {
        this.text = text;
    }

    public TextureCache getTextureCache()
    {
        return textureCache;
    }

    public void setTextureCache(TextureCache textureCache)
    {
        this.textureCache = textureCache;
    }
}
