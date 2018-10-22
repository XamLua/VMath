import org.mariuszgromada.math.mxparser.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static java.lang.Math.abs;

class Chords {

    private static double eps = 0.000001;

    private static int iter = 0;

    public static void setEps(double eps) {
        Chords.eps = eps;
    }

    public static String search(double leftBorderX, double rightBorderX, String function) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        dfs.setDecimalSeparator('.');
        DecimalFormat d = new DecimalFormat("#.#######", dfs);


        Function f = new Function("F(x) = " + function);
        Function der_2 = new Function("F(x) = der( der(" + function + ", x), x)");
        Argument x = new Argument("x = 0");
        Expression e = new Expression("F(x)", f, x);

        x.setArgumentValue(leftBorderX);
        double leftBorderY = e.calculate();

        x.setArgumentValue(rightBorderX);
        double rightBorderY = e.calculate();

        if (leftBorderX > rightBorderX || leftBorderY * rightBorderY > 0)
            return "Некорректно заданы границы.";

        double chordCrossX;

        double chordCrossY = 0;

        iter = 0;

        do {
            x.setArgumentValue(leftBorderX);
            leftBorderY = e.calculate();

            x.setArgumentValue(rightBorderX);
            rightBorderY = e.calculate();

            chordCrossX = (leftBorderX*rightBorderY - rightBorderX*leftBorderY) / (rightBorderY - leftBorderY);

            e.setArgumentValue("x", chordCrossX);
            chordCrossY = e.calculate();

            System.out.println(d.format(leftBorderX) + " " + d.format(rightBorderX) + " " +
                                d.format(chordCrossX) + " " + d.format(leftBorderY) + " " +
                                d.format(rightBorderY) + " " + d.format(chordCrossY) + " "
                    + d.format(Math.abs(leftBorderX - rightBorderX)));

            if (chordCrossY * leftBorderY < 0)
                rightBorderX = chordCrossX;
            else
                leftBorderX = chordCrossX;

            iter++;
        }

        while (Math.abs(leftBorderX - rightBorderX) > eps && Math.abs(chordCrossY) > eps);

        return "x = " + d.format(chordCrossX) + " i = " + iter;
    }

}
