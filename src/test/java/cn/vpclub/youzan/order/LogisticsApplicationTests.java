package cn.vpclub.youzan.order;

import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.RefunPay;
import cn.vpclub.youzanorder.utils.vaild.DataValidUtils;
import com.baomidou.mybatisplus.annotations.TableName;
import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ApplicationConfig.class})
public class LogisticsApplicationTests {


    @Test
    public void er() {
        String spring_profile = System.getenv().get("SPRING_PROFILES_ACTIVE");
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
    }


    @Test
    public void ss() throws JSONException {
        List<String> list = new ArrayList<String>(2);
        list.add("guan");
        list.add("bao");
        String[] array = new String[list.size()];
        array = list.toArray(array);

    }

    @Test
    public void contextLoads() {
        RefunPay pay = new RefunPay();
        pay.setOid(1233L);
        pay.setTid("wer");
        pay.setRefund_id("234");
        DataValidUtils.Result res = DataValidUtils.validation(pay);
        System.out.println(1);
    }

    @Test
    public void tt() throws Exception {
//        SubOrder su = new SubOrder();
//        Annotation[] anno = SubOrder.class.getAnnotations();
        TableName tab = SubOrder.class.getAnnotation(TableName.class);
//        InvocationHandler h = Proxy.getInvocationHandler(tab);
//        Field hField = h.getClass().getDeclaredField("memberValues");
//        hField.setAccessible(true);
//        Map memberValues = (Map) hField.get(h);
//        // 修改 value 属性值
//        memberValues.put("value", "sub_order_2018");
        System.out.println(1);
    }

    @Test
    public void tt2() {
        List<Object> sourList = new ArrayList<>(600);
        for (int i = 0; i < 500; i++) {
            sourList.add(i);
        }
        int batchCount = 22;
        List<Object> tempList = new ArrayList<Object>();
        int time=1;
        for (int i = 0; i < sourList.size(); i++) {
            tempList.add(sourList.get(i));
            if((i+1)%batchCount==0 || (i+1)==sourList.size()){
                System.out.println(tempList);
                System.out.println(time++);
                tempList.clear();
            }
        }
    }
}
