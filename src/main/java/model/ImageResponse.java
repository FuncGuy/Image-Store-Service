package model;

import java.io.InputStream;

public class ImageResponse {

    private byte[] imageData;
    private String status;


    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ImageResponse(byte[] imageData, String status) {
        this.imageData = imageData;
        this.status = status;
    }
}
