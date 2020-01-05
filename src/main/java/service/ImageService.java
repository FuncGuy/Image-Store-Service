package service;

import exceptions.FileStorageException;
import model.DeleteImageResponse;
import model.ImageResponse;
import model.UploadImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static constants.Constants.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.cleanPath;

@Service
public class ImageService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public boolean storeImage(MultipartFile file, String albumName) {
        String fileName = cleanPath(file.getOriginalFilename());
        try {
            checkIfFileNameIsValid(fileName);
            writeImageToAlbum(file, albumName);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void writeImageToAlbum(MultipartFile file, String albumName) throws IOException {

        byte[] bytes = file.getBytes();
        Path path = Paths.get(Album_Directory + albumName + "/" + file.getOriginalFilename());
        Files.write(path, bytes);

    }

    private void checkIfFileNameIsValid(String fileName) {
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
    }

    public UploadImageResponse getUploadImageResponse(String albumName, boolean isUploaded, String fileName) {
        return isUploaded ? new UploadImageResponse(fileName + Upload_Success) :
                new UploadImageResponse(Upload_Failed + albumName + Does_Not_Exist);
    }

    public boolean deleteImage(String albumName, String imageName) {

        Path path = Paths.get(Album_Directory + albumName + "/" + imageName);
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public DeleteImageResponse getDeleteImageResponse(boolean isDeleted, String albumName, String imageName) {
        return isDeleted ? new DeleteImageResponse(imageName + Deleted_From_Album + albumName) : new DeleteImageResponse(Unable_To_Delete_Image + imageName + Does_Not_Exists_In_Album);
    }

    public ImageResponse getImage(String albumName, String imageName) {
        ImageResponse imageResponse;
        String imagePath = Album_Directory + albumName + "/" + imageName;
        try {
            File image = new File(imagePath);
            byte[] fileContent = Files.readAllBytes(image.toPath());
            imageResponse = new ImageResponse(fileContent, "found");
        } catch (IOException e) {
            imageResponse = new ImageResponse(null, "not found");
        }
        return imageResponse;
    }

    public void checkImageAvailable(String status) {
        if (!"found".equals(status)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "image not found"
            );
        }
    }

    public List<String> getAlbumImages(String albumName) {
        String albumPath = Album_Directory + albumName;
        File[] files = new File(albumPath).listFiles();
               return stream(files)
                .map(image -> createImageURL(albumName, image.getName()))
                .collect(toList());


    }

    private String createImageURL(String albumName, String imageName) {
        return "http://localhost:8080/imageService/image?album-name=" + albumName
                + "&image-name=" + imageName;
    }

    public void sendNotification(boolean isSuccess, String message) {
        if(isSuccess){
            kafkaTemplate.send("first_topic", message);
        }
    }
}
