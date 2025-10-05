package nesalmanov.ru.chatservice.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    public static ErrorResponse isError(String errorText) {
        return new ErrorResponse(errorText);
    }
}
