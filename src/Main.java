public class Main {
    public void main () {
        RemainderCalc calc = new RemainderCalc();
        double firstArgument = (double) 260.42 / 360;
        double secondArgument = (double) 31.29 / 360;
        double testCase3 = calc.calculateTurretAngle(firstArgument,secondArgument);
        System.out.println(testCase3);
    }
}
