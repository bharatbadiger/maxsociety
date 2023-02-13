package co.bharat.maxsociety.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseData<T> {
    private String message;
    private int statusCode;
    private T data;
}
