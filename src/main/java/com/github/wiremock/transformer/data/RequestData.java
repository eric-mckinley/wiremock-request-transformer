package com.github.wiremock.transformer.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class RequestData implements Serializable{

    private String schema;
    private String server;
    private String port;
    private String clientIp;
    private String url;
    private String absoluteUrl;
    private String path;
    private String method;

    private List<NameValueData> params;
    private List<NameValueData> headers;
    private List<NameValueData> cookies;

    private List<String> paramNames;
    private Map<String, String> param;
    private List<String> headerNames;
    private Map<String, String> header;
    private List<String> cookieNames;
    private Map<String, String> cookie;
}
