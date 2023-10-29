package wanted.n.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import wanted.n.exception.ErrorCode;

import java.util.List;

@Getter
@ApiModel(value = "hot hashtag Response", description = "hot hashtag 결과를 반환하는 응답 정보")
public class HashtagsResponseDTO {

    private final int status;
    private final String message;
    private List<Object> hashtags;

    public HashtagsResponseDTO(ErrorCode code, List<Object> hashtags) {
        this.status = code.getStatus().value();
        this.message = code.getMessage();
        this.hashtags = hashtags;
    }
}
