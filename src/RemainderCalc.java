public class RemainderCalc {
    int leftTcount;
    int rightTcount;
    int sunTcount;
    double lRatio;
    double rRatio;
    public RemainderCalc (int leftTcount, int rightTcount,
                          int sunTcount) {
        this.leftTcount = leftTcount;
        this.rightTcount = rightTcount;
        this.sunTcount = sunTcount;
        lRatio = (double) sunTcount / leftTcount;
        rRatio = (double) sunTcount / rightTcount;
    }
    public double calculatePos (double leftR, double rightR) {
        if (leftR == 0 || rightR == 0) return 0;
        double out = ((leftR - rightR + 360) % 360) / (lRatio - rRatio);
        return out;
    }


}
