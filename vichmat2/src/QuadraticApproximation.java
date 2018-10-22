import java.util.ArrayList;
import java.util.Arrays;

public class QuadraticApproximation {

    private double a;
    private double b;
    private static double c;
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

    public double getC() {
        return c;
    }

    public void execute(ArrayList<Double> xs, ArrayList<Double> ys) {

        int n = xs.size();
        if (n == 1) {
            b = 0;
            a = 1;
            c = ys.get(0) - xs.get(0) * xs.get(0);
        } else {

            double[][] as = new double[3][3];
            double[] bs = new double[3];

            as[0][0] = n;

            for (double el : xs) {
                as[0][1] += el;
                as[1][0] += el;
            }

            for (double el : xs) {
                as[0][2] += el * el;
                as[1][1] += el * el;
                as[2][0] += el * el;
            }

            for (double el : xs) {
                as[1][2] += Math.pow(el, 3);
                as[2][1] += Math.pow(el, 3);
            }

            for (double el : xs)
                as[2][2] += Math.pow(el, 4);

            for (double el : ys)
                bs[0] += el;

            for (int i = 0; i < n; i++)
                bs[1] += xs.get(i) * ys.get(i);

            for (int i = 0; i < n; i++)
                bs[2] += Math.pow(xs.get(i), 2) * ys.get(i);

            double D = calculateDeterminant(as);
            double[][] arr1 = {
                    {bs[0], as[0][1], as[0][2]},
                    {bs[1], as[1][1], as[1][2]},
                    {bs[2], as[2][1], as[2][2]}
            };

            double[][] arr2 = {
                    {as[0][0], bs[0], as[0][2]},
                    {as[1][0], bs[1], as[1][2]},
                    {as[2][0], bs[2], as[2][2]}
            };

            double[][] arr3 = {
                    {as[0][0], as[0][1], bs[0]},
                    {as[1][0], as[1][1], bs[1]},
                    {as[2][0], as[2][1], bs[2]}
            };

            double Dc = calculateDeterminant(arr1);
            double Db = calculateDeterminant(arr2);
            double Da = calculateDeterminant(arr3);

            a = Da / D;
            b = Db / D;
            c = Dc / D;
        }
        S = 0;
        for (int i = 0; i < n; i++)
            S += Math.pow(calculate(xs.get(i)) - ys.get(i), 2);

        sigma = Math.sqrt(S / n);
    }

    public double calculate(double x) {
        return a * Math.pow(x, 2) + b * x + c;
    }

    public static double calculateDeterminant(double[][] matrix) {
        return matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][2] * matrix[2][0] + matrix[0][2] * matrix[1][0] * matrix[2][1] -
                matrix[0][2] * matrix[1][1] * matrix[2][0] - matrix[0][1] * matrix[1][0] * matrix[2][2] - matrix[0][0] * matrix[1][2] * matrix[2][1];

    }

    public String toExpression() {
        return String.valueOf(a) + "*x^2" + "+" + String.valueOf(b) + "*x" + "+" + String.valueOf(c);
    }

}
