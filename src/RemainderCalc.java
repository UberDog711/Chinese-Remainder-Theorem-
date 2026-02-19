import java.util.Arrays;

public class RemainderCalc {
    int DRIVEN_GEAR_TOOTH_COUNT = 85;
    int PLANETARY_GEAR_1_TOOTH_COUNT = 14;
    int PLANETARY_GEAR_2_TOOTH_COUNT = 13;
    int POSSIBLE_POS_ACC_DIGITS = 14;
    public double calculateTurretAngle(double encoder1Pos, double encoder2Pos) {
        double[] encoder1Positions = new double[POSSIBLE_POS_ACC_DIGITS];
        double[] encoder2Positions = new double[POSSIBLE_POS_ACC_DIGITS];
        double out = 0;
        double minValue = 1;
        for (int i = 0; i < POSSIBLE_POS_ACC_DIGITS; i++) {
            encoder1Positions[i] = (i + (encoder1Pos)) * PLANETARY_GEAR_1_TOOTH_COUNT/DRIVEN_GEAR_TOOTH_COUNT;      //0 - 1
            encoder2Positions[i] = (i + (encoder2Pos)) * PLANETARY_GEAR_2_TOOTH_COUNT/DRIVEN_GEAR_TOOTH_COUNT;
        }

        for (int i = 0; i < POSSIBLE_POS_ACC_DIGITS; i++) {
            for (int z = i; z < POSSIBLE_POS_ACC_DIGITS; z++) {
                if (Math.abs(encoder1Positions[i] - encoder2Positions[z]) < minValue) {
                    out = (encoder1Positions[i] + encoder2Positions[z]) / 2;
                    minValue = Math.abs(encoder1Positions[i] - encoder2Positions[z]);
                }
            }
        }




        return out;
    }


}
