package model;

public class ImageResponse {

    private byte[] imageData;
    private String status;


    public ImageResponse(byte[] imageData, String status) {
        this.imageData = imageData;
        this.status = status;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getStatus() {
        return status;
    }
}
