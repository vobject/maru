package maru.map.jobs.gl;

import javax.media.opengl.GL2;

import maru.map.jobs.DrawJob;

import com.jogamp.opengl.util.awt.TextRenderer;

public abstract class GLDrawJob extends DrawJob
{
    private GL2 gl;
    private TextRenderer text;
    private TextureCache textureCache;

    public GL2 getGL()
    {
        return gl;
    }

    public void setGL(GL2 gl)
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
