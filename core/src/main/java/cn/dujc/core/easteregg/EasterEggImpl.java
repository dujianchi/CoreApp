package cn.dujc.core.easteregg;

public class EasterEggImpl implements IEasterEgg {

    @Override
    public boolean canOpen() {
        return false;
    }

    @Override
    public void open() {

    }

    @Override
    public int[] trigger() {
        return new int[]{IEasterEgg.TAP, IEasterEgg.TAP, IEasterEgg.TAP, IEasterEgg.SLIDE};
    }
}
