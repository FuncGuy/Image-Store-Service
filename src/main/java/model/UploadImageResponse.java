package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadImageResponse {
    private String status;

    public UploadImageResponse(String status) {
        this.status = status;

    }
}
