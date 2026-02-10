public class RemainderCalc {
    int leftTcount;
    int rightTcount;
    int sunTcount;
    double lRatio;
    double rRatio;
    int DRIVEN_GEAR_TOOTH_COUNT = 85;
    int PLANETARY_GEAR_1_TOOTH_COUNT = 14;
    int PLANETARY_GEAR_2_TOOTH_COUNT = 13;

    double GEAR_TOOTH_CLEARANCE = 0.004; // the thickness of a sheet of 20lb paper
    double ENCODER_RANGE = 4096.0;  // 12-bit precision
    int ENCODER_RANGE_MODULUS = (int) ENCODER_RANGE;

    double GEAR_DIAMETRAL_PITCH = 10.0;    // Ratio of number of teeth to pitch diameter, that is a 10-tooth

    // Compute the maximum number of rotations (+1) for each planetary gear for a full rotation of
    // the driven gear
    double Gear1MaxRotations = (int) (DRIVEN_GEAR_TOOTH_COUNT/PLANETARY_GEAR_1_TOOTH_COUNT) + 1;
    double Gear2MaxRotations = (int) (DRIVEN_GEAR_TOOTH_COUNT/PLANETARY_GEAR_2_TOOTH_COUNT) + 1;

    // Compute the pitch diameters for the various gears
    double DrivenGearDiameter = (double) (DRIVEN_GEAR_TOOTH_COUNT) / GEAR_DIAMETRAL_PITCH;
    double PlanetaryGear1PitchDiameter = (double) (PLANETARY_GEAR_1_TOOTH_COUNT) / GEAR_DIAMETRAL_PITCH;
    double PlanetaryGear2PitchDiameter = (double) (PLANETARY_GEAR_2_TOOTH_COUNT) / GEAR_DIAMETRAL_PITCH;
    double DrivenGearDistancePerDegree = DrivenGearDiameter / 360.0;
    double DrivenGearDegreePerInch = 1.0 / DrivenGearDistancePerDegree;

    // Compute the encoder bit per unit distance for each planetary gear
    double PlanetaryGear1BitPerInch = ENCODER_RANGE / PlanetaryGear1PitchDiameter;
    double PlanetaryGear2BitPerInch = ENCODER_RANGE / PlanetaryGear2PitchDiameter;
    double PlanetaryGear1InchPerBit = 1.0 / PlanetaryGear1BitPerInch;
    double PlanetaryGear2InchPerBit = 1.0 / PlanetaryGear2BitPerInch;
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
    public double calculatePosM2(double leftDeg, double rightDeg) {
        leftDeg = (leftDeg % 360 + 360) % 360;
        rightDeg = (rightDeg % 360 + 360) % 360;
        double diff = Math.IEEEremainder(leftDeg - rightDeg, 360.0);

        double ratioDiff = lRatio - rRatio;
        if (Math.abs(ratioDiff) < 1e-6) {
            throw new IllegalArgumentException("Encoder gear ratios too close or identical");
        }

        double turretTurns = diff / ratioDiff;

        // ← round to nearest full turret turn
        double turretTurnsRounded = Math.round(turretTurns);

        double turretAngle = leftDeg + turretTurnsRounded * lRatio * 360.0;

        // optional: normalize to [0, 360)
        turretAngle = (turretAngle % 360 + 360) % 360;

        return turretAngle;
    }

    public double calculateTurretAngleFromEncoderInputs(double encoder1, double encoder2) {
        // Inputs are integer readings from the encoders, so convert
        double e1 = (encoder1);
        double e2 = (encoder2);
        // Handle an edge case near 0 degrees where one or both encoder is 'below zero', that is, has
        // a large value
        double e1Rotations = -1;
        double e2Rotations = -1;
        // Now search through all possible rotation pairs to find the smallest error
        double minError = DrivenGearDiameter;
        double minE1Rotations = e1Rotations;
        while (e1Rotations < Gear1MaxRotations && e2Rotations < Gear2MaxRotations) {
            double e1Value = e1 + (double)(e1Rotations) * ENCODER_RANGE;
            double e2Value = e2 + (double)(e2Rotations) * ENCODER_RANGE;
            double e1Distance = e1Value * PlanetaryGear1InchPerBit;
            double e2Distance = e2Value * PlanetaryGear2InchPerBit;
            double err = Math.abs(e1Distance - e2Distance);
            if (err < minError) {
                minError = err;
                minE1Rotations = e1Rotations;
            }

            if (e1Distance < e2Distance) {
                e1Rotations += 1;
            } else {
                e2Rotations += 1;
            }
        }
        //Now the best answer is given by minE1Rotations, so compute the distance gear 1
        //has moved and convert to the corresponding driven gear movement.Note that
        //the distance gear 1 has moved (at the pitch circle)is equal to the distance
        //the driven gear has moved at its pitch circle
        double e1Count = e1 + (minE1Rotations) * ENCODER_RANGE;
        double e1Distance = e1Count * PlanetaryGear1InchPerBit;
        double turretAngle = e1Distance * DrivenGearDegreePerInch;
        return turretAngle;
    }
}
