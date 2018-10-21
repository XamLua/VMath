import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static java.lang.Math.abs;

public class Secant {

    private static double eps = 0.001;

    public static void setEps(double eps){

        Secant.eps = eps;

    }

    public static String search(double leftBorderX, double rightBorderX, String function) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        dfs.setDecimalSeparator('.');
        DecimalFormat d = new DecimalFormat("#.######", dfs);


        Function f = new Function("F(x) = " + function);
        Function der_2 = new Function("F(x) = der( der(" + function + ", x), x)");
        Argument x = new Argument("x = 0");
        Expression e = new Expression("F(x)", f, x);
        Expression e_2 = new Expression("F(x)", der_2, x);

        x.setArgumentValue(leftBorderX);
        double leftBorderY = e.calculate();
        double leftBorderDer_2 = e_2.calculate();

        x.setArgumentValue(rightBorderX);
        double rightBorderY = e.calculate();
        double rightBorderDer_2 = e_2.calculate();

        double previousX = 0;
        double currentX = 0;
        double nextX = 0;
        double previousY = 0;
        double currentY = 0;
        double nextY = 0;

        if (leftBorderX > rightBorderX || leftBorderY * rightBorderY > 0) {
            return "Некорректно заданы границы.";
        }

        if (leftBorderY * leftBorderDer_2 > 0)
            previousX = leftBorderX;
        else if (rightBorderY * rightBorderDer_2 > 0)
            previousX = rightBorderX;
        else
            return "Некорректно заданы границы, вторая производная не сохраняет знак.";

        currentX = (leftBorderX + rightBorderX) / 2;

        do {
            x.setArgumentValue(previousX);
            previousY = e.calculate();

            x.setArgumentValue(currentX);
            currentY = e.calculate();

            nextX = currentX - (currentX - previousX)*currentY/(currentY - previousY);

            e.setArgumentValue("x", nextX);
            nextY = e.calculate();

            System.out.println(d.format(previousX) + " " + d.format(previousY) + " " + d.format(currentX)
                    + " " + d.format(currentY) + " " + d.format(nextX) + " " + d.format(nextY) +
                    " " + d.format(abs(nextX - currentX)));

            previousX = currentX;
            currentX = nextX;

        }

        while (abs(currentX - previousX) > eps && Math.abs(nextY) > eps);

        return "x = " + d.format(currentX);
    }

}
