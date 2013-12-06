package maru.map.views.gl;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;

public final class GLUtils
{
    public static void setupGL(GL gl, int width, int height)
    {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU glu = new GLU();
        glu.gluOrtho2D(0.0, width, 0.0, height);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glViewport(0, 0, width, height);
    }

    public static void drawTexture(GL gl, Texture texture, int x, int y, int width, int height)
    {
        texture.enable();
        texture.bind();

        gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2d(0.0, 1.0);
            gl.glVertex2d(x, y);

            gl.glTexCoord2d(1.0, 1.0);
            gl.glVertex2d(x + width, y);

            gl.glTexCoord2d(1.0, 0.0);
            gl.glVertex2d(x + width, y + height);

            gl.glTexCoord2d(0.0, 0.0);
            gl.glVertex2d(x, y + height);
        gl.glEnd();

        texture.disable();
    }

    public static void drawText(TextRenderer renderer, int width, int height,
                                int posX, int posY, String text, boolean outline)
    {
        renderer.beginRendering(width, height);

        if (outline)
        {
            // draw text outline in black
            renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);
            renderer.draw(text, posX - 1, posY - 1);
            renderer.draw(text, posX - 1, posY);
            renderer.draw(text, posX - 1, posY + 1);

            renderer.draw(text, posX + 1, posY - 1);
            renderer.draw(text, posX + 1, posY);
            renderer.draw(text, posX + 1, posY + 1);
        }

        // draw the actual text in white
        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        renderer.draw(text, posX, posY);
        renderer.endRendering();
    }
}
