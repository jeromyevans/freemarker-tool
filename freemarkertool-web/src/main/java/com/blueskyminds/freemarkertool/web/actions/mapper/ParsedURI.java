package com.blueskyminds.freemarkertool.web.actions.mapper;

/**
 * A URI that has been parsed into its components
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class ParsedURI {

    private String method;
    private String path;
    private String query;

    public ParsedURI(String method, String path, String query) {
        this.method = method;
        this.path = path;
        this.query = query;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getFile() {
        if (path.indexOf("/") >= 0) {
            return path.substring(path.lastIndexOf("/")+1);
        } else {
            return path;
        }
    }

    public String getExtension() {
        if (path.indexOf(".") >= 0) {
            return path.substring(path.lastIndexOf("."));
        } else {
            return "";
        }
    }

    public String toString() {
        return "ParsedURI[method:"+method+" path:"+path+" query:"+query+"]";
    }
}
