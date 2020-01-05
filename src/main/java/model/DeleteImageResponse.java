package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteImageResponse {

    private String status;

    public DeleteImageResponse(String status) {
        this.status = status;
    }
}
