package maru.map.views.gl;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import maru.map.MaruMapPlugin;
import maru.map.views.AbstractGLView;
import maru.map.views.IMapDrawer;
import maru.map.views.MapViewParameters;
import maru.ui.MaruUIPlugin;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;
import maru.ui.model.UiVisibleElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.GLReadBufferUtil;
import com.jogamp.opengl.util.texture.TextureData;

public class MapGLView extends AbstractGLView
{
    private IMapDrawer mapDrawer;

    @Override
    public void createPartControl(final Composite parent)
    {
        setContainer(new Composite(parent, SWT.EMBEDDED));
        getContainer().setLayout(new FillLayout());

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        setWindow(GLWindow.create(caps));
        getWindow().setVisible(true, true);

        NewtCanvasAWT canvas = new NewtCanvasAWT(getWindow());
        SWT_AWT.new_Frame(getContainer()).add(canvas);

        addEventListener();

        MaruUIPlugin.getDefault().getUiModel().addUiProjectModelListener(this);
        MaruUIPlugin.getDefault().getUiModel().addUiProjectSelectionListener(this);

        redraw();
    }

    @Override
    public void dispose()
    {
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectSelectionListener(this);
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectModelListener(this);

        if (getMapDrawer() != null) {
            getMapDrawer().dispose();
            setMapDrawer(null);
        }

        super.dispose();
    }

    @Override
    public void projectChanged(UiProject project)
    {
        redraw();
    }

    @Override
    public void projectRemoved(UiProject project)
    {
        if (project != UiModel.getDefault().getCurrentUiProject()) {
            return;
        }

        // get rid of references to the project that will be deleted
        getMapDrawer().setSelectedElement(null);
        redraw();
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        getMapDrawer().setSelectedElement(null);
        getMapDrawer().getParameters().setSettingsChanged(true);
        getMapDrawer().getSettings().setSettingsChanged(true);
        redraw();
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        if (element instanceof UiVisibleElement) {
            getMapDrawer().setSelectedElement(((UiVisibleElement) element).getUnderlyingElement());
        } else {
            getMapDrawer().setSelectedElement(null);
        }
        redraw();
    }

    public IMapDrawer getMapDrawer()
    {
        return mapDrawer;
    }

    public void setMapDrawer(IMapDrawer mapDrawer)
    {
        this.mapDrawer = mapDrawer;
    }

    @Override
    protected void addEventListener()
    {
        final MapGLView thisRef = this;

        getWindow().addGLEventListener(new GLEventListener()
        {
            @Override
            public void init(GLAutoDrawable drawable)
            {
                GL2 gl = drawable.getContext().getGL().getGL2();

                gl.glDisable(GL2.GL_LIGHTING);
                gl.glDisable(GL2.GL_DEPTH_TEST);
                gl.glDisable(GL2.GL_MULTISAMPLE);
                gl.glEnable(GL2.GL_BLEND);
                gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
                gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_FASTEST);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

                setMapDrawer(new MapGLDrawer(thisRef, drawable.getContext()));
            }

            @Override
            public void dispose(GLAutoDrawable drawable)
            {

            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
            {
                GLUtils.setupGL(drawable.getContext().getGL().getGL2(), width, height);
                getMapDrawer().getParameters().setClientArea(width, height);
            }

            @Override
            public void display(GLAutoDrawable drawable)
            {
                UiProject project = UiModel.getDefault().getCurrentUiProject();
                if (project != null) {
                    getMapDrawer().draw(drawable.getContext(), project.getUnderlyingElement());
                } else {
                    getMapDrawer().draw(drawable.getContext());
                }

                //if (MaruMapPlugin.getDefault().hasTextureListeners())
                {
                    MapViewParameters mapParams = getMapDrawer().getParameters();
                    GLReadBufferUtil bufUtil = new GLReadBufferUtil(false, false);
                    boolean read = bufUtil.readPixels(drawable.getGL(),
                                                      mapParams.mapX,
                                                      mapParams.mapY,
                                                      mapParams.mapWidth,
                                                      mapParams.mapHeight,
                                                      false);
                    if (read) {
                        TextureData data = bufUtil.getTextureData();



//                        int pixelFormat = data.getPixelFormat();
//                        int pixelType   = data.getPixelType();
//
//                        if ((pixelFormat != GL.GL_RGB) || (pixelType != GL.GL_UNSIGNED_BYTE)) {
//                            return;
//                        }
//
//                        BufferedImage img = new BufferedImage(data.getWidth(), data.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//                        byte[] imgData = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
//
//                        ByteBuffer buf = (ByteBuffer) data.getBuffer().rewind();
//                        buf.get(imgData).rewind();
//
//                        for (int i = 0; i < imgData.length; i += 3) {
//                            byte red  = imgData[i + 0];
//                            byte blue = imgData[i + 2];
//                            imgData[i + 0] = blue;
//                            imgData[i + 2] = red;
//                        }
//
//                        ImageUtil.flipImageVertically(img);
//                        try
//                        {
//                            ImageIO.write(img, "jpg", new File("D:\\aaaa3.jpg"));
//                        }
//                        catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }




                        MaruMapPlugin.getDefault().notifyTextureListeners(data);
                    }
                }
            }
        });

        getWindow().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                getMapDrawer().mouseEvent(e.getButton(), e.getModifiers(),
                                          e.getClickCount(), e.getX(), e.getY());
            }
        });
    }
}
