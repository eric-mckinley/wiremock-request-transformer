package com.github.wiremock.transformer.data;

import com.github.tomakehurst.wiremock.http.Cookie;
import com.github.tomakehurst.wiremock.http.Request;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestDataBuilder {

    private static final Pattern urlWithPortPattern = Pattern.compile("^(.*):\\/\\/([A-Za-z0-9\\-\\.]+):([0-9]+)");
    private static final Pattern urlWithoutPortPattern = Pattern.compile("^(.*):\\/\\/([A-Za-z0-9\\-\\.]+)");
    private static final Pattern requestParamsPattern = Pattern.compile("(\\?|\\&)([^=]+)\\=([^&]+)");

    public static RequestData build(Request request){
        RequestData data = new RequestData();

        data.setUrl(request.getUrl());
        data.setAbsoluteUrl(request.getAbsoluteUrl());
        data.setClientIp(request.getClientIp());
        data.setMethod(request.getMethod().getName());
        data.setHeaderNames(new ArrayList<String>(request.getAllHeaderKeys()));

        int  index = Math.max(request.getUrl().indexOf("?"), request.getUrl().indexOf("#"));
        if(index > 0){
            data.setPath(request.getUrl().substring(0, index));
        }
        else{
            data.setPath(request.getUrl());
        }

        Matcher schemaMatcher = urlWithoutPortPattern.matcher(request.getAbsoluteUrl());
        if (schemaMatcher.find()) {
            data.setSchema(schemaMatcher.group(1));
            data.setServer(schemaMatcher.group(2));
        }

        Matcher portMatcher = urlWithPortPattern.matcher(request.getAbsoluteUrl());
        if (portMatcher.find()) {
            data.setPort(portMatcher.group(3));
        } else {
            data.setPort("80");
        }

        List<String> paramNames = new ArrayList<String>();
        List<NameValueData> params = new ArrayList<NameValueData>();
        Map<String, String> paramMap = new HashMap<String, String>();

        Matcher paramMatcher = requestParamsPattern.matcher(request.getUrl());
        while (paramMatcher.find()) {
            paramNames.add(paramMatcher.group(2));
            params.add(new NameValueData(paramMatcher.group(2), paramMatcher.group(3)));
            paramMap.put(paramMatcher.group(2), paramMatcher.group(3));
        }
        data.setParamNames(paramNames);
        data.setParams(params);
        data.setParam(paramMap);

        List<String> cookieNames = new ArrayList<String>();
        List<NameValueData> cookies = new ArrayList<NameValueData>();
        Map<String, String> cookieMap = new HashMap<String, String>();


        Map<String, Cookie> cookiesMap = request.getCookies();
        for (Map.Entry<String, Cookie> next : cookiesMap.entrySet()) {
            cookieNames.add(next.getKey());
            cookies.add(new NameValueData(next.getKey(), next.getValue().getValue()));
            cookieMap.put(next.getKey(), next.getValue().getValue());
        }

        data.setCookieNames(cookieNames);
        data.setCookies(cookies);
        data.setCookie(cookieMap);

        List<String> headerNames = new ArrayList<String>(request.getAllHeaderKeys());
        List<NameValueData> headers = new ArrayList<NameValueData>();
        Map<String, String> headerMap = new HashMap<String, String>();

        for(String headerName: headerNames){
            headers.add(new NameValueData(headerName, request.getHeader(headerName)));
            headerMap.put(headerName, request.getHeader(headerName));
        }

        data.setHeaderNames(headerNames);
        data.setHeaders(headers);
        data.setHeader(headerMap);

        return data;
    }
}
