package controller;

import model.AlbumResponse;
import model.DeleteImageResponse;
import model.ImageResponse;
import model.UploadImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.AlbumService;
import service.ImageService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/imageService")
public class ImageController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/createAlbum")
    public AlbumResponse createAlbum(@RequestParam("album-name") String albumName) {
        return AlbumService.createAlbum(albumName);
    }

    @DeleteMapping("/deleteAlbum")
    public AlbumResponse deleteAlbum(@RequestParam("album-name") String albumName) {
        return albumService.deleteAlbum(albumName);
    }

    @PostMapping("/uploadImage")
    public UploadImageResponse createImage(@RequestParam("imageFile") MultipartFile file,
                                           @RequestParam("album-name") String albumName) {
        boolean isUploaded = imageService.storeImage(file, albumName);
        imageService.sendNotification(isUploaded, "Image " + file.getOriginalFilename()+ " created at "+ new Date());
        return imageService.getUploadImageResponse(albumName, isUploaded, file.getName());
    }


    @DeleteMapping("/deleteImage")
    public DeleteImageResponse deleteImage(@RequestParam("album-name") String albumName,
                                           @RequestParam("image-name") String imageName) {
        boolean isDeleted = imageService.deleteImage(albumName, imageName);
        imageService.sendNotification(isDeleted, "Image " + imageName +" deleted at "+ new Date());
        return imageService.getDeleteImageResponse(isDeleted, albumName, imageName);
    }

    @GetMapping("/image")
    @Cacheable(cacheNames="imageCache", key="#imageName")
    public ResponseEntity<byte[]> getImage(
            @RequestParam("album-name") String albumName,
            @RequestParam("image-name") String imageName) {

        ImageResponse imageResponse = imageService.getImage(albumName, imageName);

        imageService.checkImageAvailable(imageResponse.getStatus());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResponse.getImageData());
    }

    @GetMapping("images")
    @Cacheable(cacheNames="imageCache", key="#albumName")
    public List<String> getImages(@RequestParam("album-name") String albumName){
        return imageService.getAlbumImages(albumName);
    }

}
