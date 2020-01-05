package model;

import lombok.Getter;

@Getter
public class AlbumResponse {
    private String albumstatus;

    public AlbumResponse(String albumName, String albumstatus) {
        this.albumstatus = albumName.concat(albumstatus);
    }
}
