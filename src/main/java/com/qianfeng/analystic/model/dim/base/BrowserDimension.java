package com.qianfeng.analystic.model.dim.base;

import com.qianfeng.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//browser_dimension实体类
public class BrowserDimension extends BaseDimension {
    private int id;
    private String browserName;//浏览器名称
    private String browserVersion;//浏览器版本号

    public BrowserDimension() {
    }

    public BrowserDimension(String browser_name, String browser_version) {
        this.browserName = browser_name;
        this.browserVersion = browser_version;
    }

    public BrowserDimension(int id, String browser_name, String browser_version) {
        this.id = id;
        this.browserName = browser_name;
        this.browserVersion = browser_version;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getBrowser_name() {
        return browserName;
    }

    public void setBrowser_name(String browser_name) {
        this.browserName = browser_name;
    }

    public String getBrowser_version() {
        return browserVersion;
    }

    public void setBrowser_version(String browser_version) {
        this.browserVersion = browser_version;
    }

    @Override
    public String toString() {
        return "browser_dimension{" +
                "id=" + id +
                ", browser_name='" + browserName + '\'' +
                ", browser_version='" + browserVersion + '\'' +
                '}';
    }

    public static List<BrowserDimension> buildList(String browsrName, String browserVersion) {
        if (StringUtils.isEmpty(browsrName)) {
            browsrName = browserVersion = GlobalConstants.DEFAULT_VALUE;
        }
        if (StringUtils.isEmpty(browserVersion)) {
            browserVersion = GlobalConstants.DEFAULT_VALUE;
        }
        List<BrowserDimension> li = new ArrayList<BrowserDimension>();
        li.add(new BrowserDimension(browsrName, browserVersion));
        li.add(new BrowserDimension(browsrName, GlobalConstants.ALL_OF_VALUE));
        return li;
    }

    @Override
    public int compareTo(BaseDimension o) {
        BrowserDimension other = (BrowserDimension) o;
        if (this == other) {
            return 0;
        }
        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.browserName.compareTo(other.browserName);
        if (tmp != 0) {
            return tmp;
        }
        return this.browserVersion.compareTo(other.browserVersion);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.id);
        dataOutput.writeUTF(this.browserName);
        dataOutput.writeUTF(this.browserVersion);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.browserName = dataInput.readUTF();
        this.browserVersion = dataInput.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowserDimension browser = (BrowserDimension) o;

        if (id != browser.id) return false;
        if (browserName != null ? !browserName.equals(browser.browserName) : browser.browserName != null)
            return false;
        return browserVersion != null ? browserVersion.equals(browser.browserVersion) : browser.browserVersion == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (browserName != null ? browserName.hashCode() : 0);
        result = 31 * result + (browserVersion != null ? browserVersion.hashCode() : 0);
        return result;
    }
}
