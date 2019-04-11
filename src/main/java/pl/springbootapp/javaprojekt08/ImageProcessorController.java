package pl.springbootapp.javaprojekt08;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageProcessorController {

    private Map<String,BufferedImage> images;
    private static int id = 1;
    private static String UPLOADED_FOLDER = "D:\\DEV\\GitHub\\javaprojekt08\\src\\main\\resources\\static\\images";

    ImageProcessorController()
    {
        images = new HashMap<>();
    }

    public String set(InputStream data){
        try {
            BufferedImage buff = ImageIO.read(data);
            images.put(String.valueOf(id++), buff);
            return "uploadedSuccesfull";

        } catch (IOException e) {
            e.printStackTrace();
            return "uploadedError";
        }
    }

    public String size(String id){
        Map info = new LinkedHashMap();
        info.put("id",id);
        info.put("imageWidth",images.get(id).getWidth());
        info.put("imageHeight",images.get(id).getHeight());
        JSONObject imageInfo = new JSONObject(info);
        return imageInfo.toJSONString();
    }

    public String histogram(String id){
        Map <String,Integer>mapHistogram= new LinkedHashMap();
        for (int i=0; i < 256; i++) mapHistogram.put(String.valueOf(i),0);
        BufferedImage bi = images.get(id);
        BufferedImage buff = images.get(id);
        for(int i = 0 ; i<buff.getWidth(); i++)
        {
            for(int j = 0; j<buff.getHeight(); j++)
            {
                int value = buff.getRGB(i,j);
                int red = (value >> 16) & 0xFF;
                int green = (value >> 8) & 0xFF;
                int blue = value & 0xFF;
                value = (red+green+blue) / 3;
                int obj = mapHistogram.get(String.valueOf(value))+1;
                mapHistogram.put(String.valueOf(value),obj);
            }
        }
        JSONObject histogram = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(mapHistogram);
        histogram.put("id",id);
        histogram.put("histogram",array);
        //JSONArray arrayHistogram = new JSONArray(Arrays.asList(histogram));
        return histogram.toJSONString();
    }

    public byte[] crop(String id, int array[]) throws IOException{
        BufferedImage crop = images.get(id).getSubimage(array[0],array[1],array[2],array[3]);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( crop, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }


    public Boolean remove(String id)
    {
        return ( images.remove(id) == null ) ? false:true;
    }

    private BufferedImage fromBinaryToBufferedImage(byte[] imageData)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
