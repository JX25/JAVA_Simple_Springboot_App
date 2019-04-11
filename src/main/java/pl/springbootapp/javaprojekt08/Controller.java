package pl.springbootapp.javaprojekt08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class Controller {

    @Autowired
    ImageProcessorController imageProcessorController;



    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/image/add", method = RequestMethod.POST)
    public String addImage(HttpServletRequest requestEntity) throws Exception {
        String info = imageProcessorController.set(requestEntity.getInputStream());
        return info;
    }


    @RequestMapping(value = "/image/{id}/size", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String imageSize(@PathVariable String id){
        String info = imageProcessorController.size(id);
        return info;
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteImage(@PathVariable String id){
        if( imageProcessorController.remove(id) != false ) return "deleted image id:"+id;
        else return "cannot deleted image id:"+id;
    }


    @RequestMapping(value = "/image/{id}/crop", method = RequestMethod.GET, produces =
            MediaType.IMAGE_PNG_VALUE)
    public byte[] cropImage(@PathVariable String id, @RequestParam("width") int width,
                            @RequestParam("height") int height, @RequestParam("Xstart") int x,
                            @RequestParam("Ystart") int y
                            ) throws Exception {
        int array[] = {x,y,width,height};
        return imageProcessorController.crop(id, array);
    }

    @RequestMapping(value = "/image/{id}/histogram", method = RequestMethod.GET,produces = "application/json")
    public String histogramImage(@PathVariable String id) throws Exception {
        return imageProcessorController.histogram(id);
    }


    @ExceptionHandler({IllegalArgumentException.class, Exception.class})
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "no image with this id");
    }
}


