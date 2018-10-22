import java.util.ArrayList;

public class LinearApproximation {

    private double a;
    private double b;
    private double S;
    private double sigma;

    public double getS() {
        return S;
    }

    public double getSigma() {
        return sigma;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public void execute(ArrayList<Double> xs, ArrayList<Double> ys){

        int n = xs.size();

        if (n == 1){
            a = xs.get(0) != 0 ? ys.get(0)/xs.get(0) : 1;
            b = xs.get(0) != 0 ? 0 : ys.get(0) - xs.get(0);
        }

        else {
            double SX = 0;
            double SXX = 0;
            double SY = 0;
            double SXY = 0;

            for (int i = 0; i < n; i++) {
                SX += xs.get(i);
                SXX += xs.get(i) * xs.get(i);
                SY += ys.get(i);
                SXY += xs.get(i) * ys.get(i);
            }

            a = (SXY * n - SX * SY) / (SXX * n - SX * SX);

            b = (SXX * SY - SX * SXY) / (SXX * n - SX * SX);
        }

        S = 0;
        for (int i = 0; i < n; i++)
            S += Math.pow(calculate(xs.get(i)) - ys.get(i), 2);

        sigma = Math.sqrt(S/n);
    }

    public double calculate(double x){
        return  a * x + b;
    }

    public String toExpression(){
        return String.valueOf(a) + "*x" + "+" + String.valueOf(b);
    }

}
