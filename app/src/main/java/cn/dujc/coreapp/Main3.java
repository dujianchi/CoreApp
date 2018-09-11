package cn.dujc.coreapp;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.GodDeserializer;
import cn.dujc.core.util.GsonUtil;
import cn.dujc.coreapp.line.BrokenLineView;

/**
 * @author du
 * date 2018/9/7 下午5:34
 */
public class Main3 extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.main3;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        BrokenLineView lineView = findViewById(R.id.lineView);
        List<Integer> list = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            list.add(index * 10);
        }
        lineView.setData(list);

        final List<String> abc = Arrays.asList("str0","str1");
        final CE ce = new CE();
        ce.setAbc(abc);
        ce.setGod(new String[]{"321","654","987"});
        final BE be = new BE();
        be.zz = 123;
        be.ww = Arrays.asList(1,2,3,4,5,0);
        be.vv = new ArrayList<>();
        be.vv.add(ce);
        final AE ae = new AE();
        ae.aa = "aa";
        ae.bb = "bb";
        ae.cc = "cc";
        ae.dd = 12345;
        ae.ff = 0.5f;
        ae.hh = be;
        ae.ii = new BE();

        final String json = GsonUtil.toJsonString(ae);//{"aa":"aa","bb":"bb","cc":"cc","dd":12345,"ee":null,"ff":0.5,"gg":0.0,"hh":{"vv":[{"whoAmI":["str0","str1"]}],"ww":[1,2,3,4,5,0],"xx":null,"yy":false,"zz":123},"ii":{"vv":null,"ww":null,"xx":null,"yy":false,"zz":0}}
        System.out.println(json);
        final String jsonStr = "{\"aa\":{},\"bb\":\"bb\",\"cc\":\"cc\",\"dd\":12345,\"ee\":null,\"ff\":0.5,\"gg\":0.0,\"hh\":{\"vv\":[{\"whoAmI\":[\"str0\",\"str1\"],\"god\":[\"321\",\"654\",\"987\"]}],\"ww\":[1,2,3,4,5,0],\"xx\":\"Yes\",\"yy\":false,\"zz\":123},\"ii\":{\"vv\":null,\"ww\":null,\"xx\":null,\"yy\":false,\"zz\":0}}";
        final Gson gson = new GsonBuilder().registerTypeAdapter(AE.class, new GodDeserializer<AE>()).create();
        final AE result = gson.fromJson(jsonStr, AE.class);
        System.out.println("aa="+result.getAa());
        System.out.println("bb="+result.getBb());
        System.out.println("cc="+result.getCc());
        System.out.println("dd="+result.getDd());
        System.out.println("ee="+result.getEe());
        System.out.println("ff="+result.getFf());
        System.out.println("gg="+result.getGg());
        final BE hh = result.getHh();
        System.out.println("hh="+hh);
        if (hh != null){
            System.out.println("hh.zz="+hh.zz);
            System.out.println("hh.yy="+hh.yy);
            System.out.println("hh.xx="+hh.xx);
            final List<CE> vv = hh.vv;
            System.out.println("hh.vv="+vv);
            if (vv != null) {
                for(CE c : vv){
                    System.out.println("hh.vv.[].abc="+c.abc);
                    final String[] god = c.god;
                    System.out.println("hh.vv.[].god="+Arrays.toString(god));
                    if(god != null){
                        for (String g : god) System.out.println("hh.vv.[].god[]="+g);
                    }
                }
            }
            System.out.println("hh.ww="+hh.ww);
            if (hh.ww != null){
                for (int w : hh.ww) System.out.println("hh.ww.[]="+w);
            }
        }
        final BE ii = result.getIi();
        System.out.println("ii="+ii);
        if (ii != null){
            System.out.println("ii.zz="+ii.zz);
            System.out.println("ii.yy="+ii.yy);
            System.out.println("ii.xx="+ii.xx);
            final List<CE> vv = ii.vv;
            System.out.println("ii.vv="+vv);
            if (vv != null) {
                for(CE c : vv){
                    System.out.println("ii.vv.[].abc="+c.abc);
                    final String[] god = c.god;
                    System.out.println("ii.vv.[].god="+Arrays.toString(god));
                    if(god != null){
                        for (String g : god) System.out.println("ii.vv.[].god[]="+g);
                    }
                }
            }
            System.out.println("ii.ww="+ii.ww);
            if (ii.ww != null){
                for (int w : ii.ww) System.out.println("ii.ww.[]="+w);
            }
        }
    }

    static class AE {
        private String aa, bb, cc;
        private Integer dd, ee;
        private float ff, gg;
        private BE hh, ii;

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        public String getBb() {
            return bb;
        }

        public void setBb(String bb) {
            this.bb = bb;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }

        public Integer getDd() {
            return dd;
        }

        public void setDd(Integer dd) {
            this.dd = dd;
        }

        public Integer getEe() {
            return ee;
        }

        public void setEe(Integer ee) {
            this.ee = ee;
        }

        public float getFf() {
            return ff;
        }

        public void setFf(float ff) {
            this.ff = ff;
        }

        public float getGg() {
            return gg;
        }

        public void setGg(float gg) {
            this.gg = gg;
        }

        public BE getHh() {
            return hh;
        }

        public void setHh(BE hh) {
            this.hh = hh;
        }

        public BE getIi() {
            return ii;
        }

        public void setIi(BE ii) {
            this.ii = ii;
        }
    }

    static class BE {
        public int zz;
        boolean yy;
        private Boolean xx;
        private List<Integer> ww;
        private List<CE> vv;

        public void setYy(boolean yy) {
            this.yy = yy;
        }

        public Boolean getXx() {
            return xx;
        }

        public void setXx(Boolean xx) {
            this.xx = xx;
        }

        public List<Integer> getWw() {
            return ww;
        }

        public void setWw(List<Integer> ww) {
            this.ww = ww;
        }

        public List<CE> getVv() {
            return vv;
        }

        public void setVv(List<CE> vv) {
            this.vv = vv;
        }
    }

    static class CE {
        @SerializedName("whoAmI")
        private List<String> abc;
        private String[] god;

        public void setAbc(List<String> abc) {
            this.abc = abc;
        }

        public List<String> getAbc() {
            return abc;
        }

        public String[] getGod() {
            return god;
        }

        public void setGod(String[] god) {
            this.god = god;
        }
    }
}
