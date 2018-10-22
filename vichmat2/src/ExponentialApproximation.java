import java.util.ArrayList;

public class ExponentialApproximation {

    private double a;
    private double b;

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    private double S;
    private double sigma;

    public double getS() {
        return S;
    }

    public double getSigma() {
        return sigma;
    }

    public void execute(ArrayList<Double> xs, ArrayList<Double> ys) {

        int n = xs.size();

        if(n == 1){
            a = ys.get(0);
            b = 0;
        }
        else {
            LinearApproximation la = new LinearApproximation();
            ArrayList<Double> newys = new ArrayList<>();
            for (int i = 0; i < ys.size(); i++)
                newys.add(Math.log(ys.get(i)));

            la.execute(xs, newys);

            b = la.getA();
            a = Math.pow(Math.E, la.getB());
        }
        S = 0;
        for (int i = 0; i < n; i++)
            S += Math.pow(calculate(xs.get(i)) - ys.get(i), 2);

        sigma = Math.sqrt(S/n);

    }

    public double calculate(double x){
        return  a * Math.pow(Math.E, b*x);
    }

    public String toExpression(){
        return String.valueOf(a) + "*e^{" + String.valueOf(b) + "*x" + "}";
    }

}
