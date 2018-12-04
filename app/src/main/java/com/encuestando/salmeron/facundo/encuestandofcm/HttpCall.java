package com.encuestando.salmeron.facundo.encuestandofcm;

import java.util.HashMap;

public class HttpCall {

    public static final int GET = 1;
    public static final int POST = 2;
    private String url;
    private int methodType;
    private HashMap<String, String> params;
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
