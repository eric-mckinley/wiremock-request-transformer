package com.github.wiremock.transformer.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NameValueData implements Serializable {

    private String name;
    private String value;

    public String toString(){
        return "{ name: " + name + ", value: " + value + "}";
    }
}
