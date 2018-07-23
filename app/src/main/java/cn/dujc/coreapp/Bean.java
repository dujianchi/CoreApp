package cn.dujc.coreapp;

/**
 * @author du
 * date 2018/7/14 上午11:34
 */
public class Bean {
    public byte _byte;
    public short _short;
    public char _char;
    public int _int;
    public float _float;
    public double _double;

    public Byte _Byte;
    public Short _Short;
    public Character _Character;
    public Integer _Integer;
    public Float _Float;
    public Double _Double;

    public String _String;

    public String[] _Strings;
    public int[] _ints;
    public Float[] _Floats;

    public Bean() { }

    public Bean init(){
        _byte = 1;
        _short = 2;
        _char = '3';
        _int = 4;
        _float = 5;
        _double = 6;
        _Byte = 1;
        _Short = 2;
        _Character = '3';
        _Integer = 4;
        _Float = 5f;
        _Double = 6.0;
        _String = "7";
        _Strings = new String[]{"888", "8888", "88888"};
        _ints = new int[]{9, 99, 999};
        _Floats = new Float[]{10f, 10.1f, 10.2f};
        return this;
    }
}
