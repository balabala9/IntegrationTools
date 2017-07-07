package org.util.hadoop;

import org.apache.hadoop.fs.FileStatus;

/**
 * Created by li on 17-7-5.
 */
public class Filebean {

    private String accessTime;//最后访问时间
    private String group;//文件所在分组
    private String len;//文件长度
    private String path;//路径
    private String permission;//权限
    private String owner;//文件所有者
    private String modificationTime;//最后修改时间
    private String replication;//备份
    private String blockSize;//块大小
    private String isDir;
    private String isFile;
    private FileStatus[] dirs;

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public String getIsDir() {
        return isDir;
    }

    public void setIsDir(String isDir) {
        this.isDir = isDir;
    }

    public String getIsFile() {
        return isFile;
    }

    public void setIsFile(String isFile) {
        this.isFile = isFile;
    }

    public FileStatus[] getDirs() {
        return dirs;
    }

    public void setDirs(FileStatus[] dirs) {
        this.dirs = dirs;
    }
}
