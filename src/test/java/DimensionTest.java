import com.qianfeng.analystic.model.dim.base.PlatformDimension;
import com.qianfeng.analystic.mr.service.IDimensionConvert1;
import com.qianfeng.analystic.mr.service.impl.IDimensionConvertImpl2;

public class DimensionTest {
    public static void main(String[] args) {
        PlatformDimension pl = new PlatformDimension("all");
        IDimensionConvert1 convert = new IDimensionConvertImpl2();
        System.out.println(convert.getDimensionByValue(pl));

    }
}
