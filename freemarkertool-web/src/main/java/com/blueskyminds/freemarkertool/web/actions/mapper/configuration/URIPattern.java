package com.blueskyminds.freemarkertool.web.actions.mapper.configuration;

/**
 * Defines patterns for matching a HTTP URI
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class URIPattern {

    private String id;
    private String method;
    private String path;
        
    public URIPattern(String id, String method, String path) {
        this.id = id;
        this.method = method;
        this.path = path;
    }

    public URIPattern() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
