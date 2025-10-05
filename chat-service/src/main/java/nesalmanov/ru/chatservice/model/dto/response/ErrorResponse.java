package nesalmanov.ru.chatservice.model.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends Response{
    private String errorText;

    public ErrorResponse(String errorText) {
        this.errorText = errorText;
    }
}
