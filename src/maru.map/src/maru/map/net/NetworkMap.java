package maru.map.net;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;

import maru.core.model.net.INetworkClientConnection;
import maru.map.MaruMapPlugin;
import maru.map.views.gl.ITextureListener;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.TextureData;

class MyTextureWriter
{
    static boolean write(File file, TextureData data) throws IOException
    {
        int pixelFormat = data.getPixelFormat();
        int pixelType   = data.getPixelType();

        if ((pixelFormat != GL.GL_RGB) || (pixelType != GL.GL_UNSIGNED_BYTE)) {
            throw new IOException("Doesn't support this pixel format / type");
        }

        // Convert TextureData to appropriate BufferedImage
        // FIXME: almost certainly not obeying correct pixel order
        BufferedImage image = new BufferedImage(data.getWidth(), data.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        ByteBuffer buf = (ByteBuffer) data.getBuffer();
        if (buf == null) {
            buf = (ByteBuffer) data.getMipmapData()[0];
        }
        buf.rewind();
//            byte[] imageData = new byte[buf.array().length];
        buf.get(imageData);
        buf.rewind();

        // Swizzle image components to be correct
        if (pixelFormat == GL.GL_RGB) {
            for (int i = 0; i < imageData.length; i += 3) {
                byte red  = imageData[i + 0];
                byte blue = imageData[i + 2];
                imageData[i + 0] = blue;
                imageData[i + 2] = red;
            }
        } else {
            for (int i = 0; i < imageData.length; i += 4) {
                byte red   = imageData[i + 0];
                byte green = imageData[i + 1];
                byte blue  = imageData[i + 2];
                byte alpha = imageData[i + 3];
                imageData[i + 0] = alpha;
                imageData[i + 1] = blue;
                imageData[i + 2] = green;
                imageData[i + 3] = red;
            }
        }

        // Flip image vertically for the user's convenience
        ImageUtil.flipImageVertically(image);
        return ImageIO.write(image, IOUtil.getFileSuffix(file), file);
    }
}

public class NetworkMap implements INetworkClientConnection, ITextureListener
{
    private Socket client;
    private DataOutputStream output;

    @Override
    public void open(Socket socket) throws IOException
    {
        this.client = socket;
        this.output = new DataOutputStream(new BufferedOutputStream(this.client.getOutputStream()));

        MaruMapPlugin.getDefault().addTextureListener(this);
    }

    @Override
    public void close() throws IOException
    {
        MaruMapPlugin.getDefault().removeTextureListener(this);

        if (output != null)
        {
            output.close();
            output = null;
            client = null;
        }
    }

    @Override
    public void textureUpdated(TextureData data)
    {
        if (output == null) {
            return; // the object is in closed state
        }

        int pixelFormat = data.getPixelFormat();
        int pixelType   = data.getPixelType();

        if ((pixelFormat != GL.GL_RGB) || (pixelType != GL.GL_UNSIGNED_BYTE)) {
            return;
        }

        BufferedImage img = new BufferedImage(data.getWidth(), data.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        byte[] imgData = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        ByteBuffer buf = (ByteBuffer) data.getBuffer().rewind();
        buf.get(imgData).rewind();

        for (int i = 0; i < imgData.length; i += 3) {
            byte red  = imgData[i + 0];
            byte blue = imgData[i + 2];
            imgData[i + 0] = blue;
            imgData[i + 2] = red;
        }

        ImageUtil.flipImageVertically(img);

        try
        {
            output.writeInt(data.getWidth());
            output.writeInt(data.getHeight());
            output.writeInt(BufferedImage.TYPE_3BYTE_BGR);
            output.write(imgData);
            output.flush();
            //output.
//            ImageIO.write(img, "jpg", output);

//            BufferedImage img2 = new BufferedImage(data.getWidth(), data.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//            byte[] img2Data = ((DataBufferByte) img2.getRaster().getDataBuffer()).getData();
//
//            System.arraycopy(imgData, 0, img2Data, 0, imgData.length);
//
////            BufferedImage img2 = ImageIO.read(new ByteArrayInputStream(imgData));
////            ImageIO.write(img2, "jpg", new File("D:\\aaaa5.jpg"));
//
//            ImageIO.write(img2, "jpg", new File("D:\\aaaa6.jpg"));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

//        try
//        {
//            MyTextureWriter.write(new File("D:\\aaaa2.jpg"), data);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }
}
