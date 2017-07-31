package org.util.elasticSearch;

/**
 * Created by li on 17-7-31.
 */
public class ElIndexBean {
    //索引==数据库名
    private String index;
    //类型==表明
    private String type;
    private String id;
    private long version;
    private boolean isCreated;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
