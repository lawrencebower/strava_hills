package model;

public class GradUnit {

    public float grad;
    public float gain;
    public float dist;
    public float totalDist;
    public float totalGain;

    public GradUnit(float dist,
                    float gain,
                    float totalDist,
                    float totalGain) {

        this.dist = dist;
        this.gain = gain;
        this.grad = (gain/dist)*100;
        this.totalDist = totalDist;
        this.totalGain = totalGain;
    }

    @Override
    public String toString() {
        String format = "%.2f\t%.2f\t%.2f";
        return String.format(format, dist, gain, grad);
    }
}
