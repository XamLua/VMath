import java.util.ArrayList;

public class LogarithmicApproximation {

    private static double a;
    private static double b;
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

    public void execute(ArrayList<Double> xs, ArrayList<Double> ys) {

        int n = xs.size();

        LinearApproximation la = new LinearApproximation();

        ArrayList<Double> newxs = new ArrayList<>();
        for (int i = 0; i < xs.size(); i++)
            newxs.add(Math.log(xs.get(i)));

        la.execute(newxs, ys);

        a = la.getA();
        b = la.getB();

        S = 0;
        for (int i = 0; i < n; i++)
            S += Math.pow(calculate(xs.get(i)) - ys.get(i), 2);

        sigma = Math.sqrt(S / n);
    }

    public double calculate(double x) {
        return b + a * Math.log(x);
    }

    public String toExpression(){
        return String.valueOf(b) + "+" + String.valueOf(a) + "*log(x)";
    }

}