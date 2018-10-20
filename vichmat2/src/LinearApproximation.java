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

        double SX = 0;

        for (double el: xs)
            SX += el;

        double SXX = 0;

        for (double el : xs)
            SXX += el*el;

        double SY = 0;
        for (double el : ys)
            SY += el;

        double SXY = 0;
        for (int i = 0; i < n; i++)
            SXY += xs.get(i) * ys.get(i);

        a = (SXY * n - SX * SY) / (SXX * n - SX * SX);

        b = (SXX * SY - SX * SXY) / (SXX * n - SX * SX);

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
