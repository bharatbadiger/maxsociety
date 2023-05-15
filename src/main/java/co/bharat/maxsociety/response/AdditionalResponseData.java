package co.bharat.maxsociety.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdditionalResponseData<T> {
    private String message;
    private int statusCode;
    private T additionalData;
    private T data;
}
