public class Main {
    public void main () {
        RemainderCalc calc = new RemainderCalc();
        double firstArgument = (double) 61.61 / 360;
        double secondArgument = (double) 38.67 / 360;
        double testCase3 = calc.calculateTurretAngle(firstArgument,secondArgument);
        System.out.println(testCase3);
    }
}
