package service;

import model.AlbumResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static constants.Constants.*;
import static org.apache.commons.io.FileUtils.deleteDirectory;

@Service
public class AlbumService {

    public static AlbumResponse createAlbum(String albumName) {
        boolean isAlbumCreated = new File(Album_Directory + albumName).mkdir();
        return isAlbumCreated ?
                new AlbumResponse(albumName, Album_Created_Successfully)
                : new AlbumResponse(albumName, Album_Already_Exists);
    }

    public AlbumResponse deleteAlbum(String albumName) {
        File directory = new File(Album_Directory + albumName);
        try {
            deleteDirectory(directory);
            return new AlbumResponse(albumName, Delete_Success);
        } catch (IOException e) {
            return new AlbumResponse(albumName, Unable_To_Delete
                    + e.getMessage());
        }
    }
}
