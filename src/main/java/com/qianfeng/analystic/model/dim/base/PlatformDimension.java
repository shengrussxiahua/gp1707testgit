package com.qianfeng.analystic.model.dim.base;

import com.qianfeng.common.GlobalConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlatformDimension extends BaseDimension {
    private Integer id;

    private String platformName;

    public PlatformDimension() {
    }

    public PlatformDimension(String platformName) {
        this.platformName = platformName;
    }

    public PlatformDimension(Integer id, String platformName) {
        this(platformName);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName == null ? null : platformName.trim();
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }

        PlatformDimension other = (PlatformDimension) o;

        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }

        return this.platformName.compareTo(other.platformName);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeUTF(this.platformName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.platformName = in.readUTF();

    }

    public static List<PlatformDimension> buildList(String platformName) {
        if (StringUtils.isEmpty(platformName)) {
            platformName = GlobalConstants.DEFAULT_NAME;
        }

        List<PlatformDimension> li = new ArrayList<>();

        li.add(new PlatformDimension(platformName));
        li.add(new PlatformDimension(GlobalConstants.ALL_OF_VALUE));

        return li;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlatformDimension that = (PlatformDimension) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(platformName, that.platformName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, platformName);
    }
}
