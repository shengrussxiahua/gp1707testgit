import com.qianfeng.analystic.model.dim.base.PlatformDimension;
import com.qianfeng.etil.mr.service.IDimensionConvert;
import com.qianfeng.etil.mr.service.impl.IDimensionConvertImpl;

public class DimensionTest {
    public static void main(String[] args) {
        PlatformDimension pl = new PlatformDimension("all");
        IDimensionConvert convert = new IDimensionConvertImpl();
        System.out.println(convert.getDimensionByValue(pl));

    }
}
