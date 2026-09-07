package demo;


import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

   
    static class Point {

        BigInteger x;
        BigInteger y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }


    

    static class Fraction {

        BigInteger numerator;
        BigInteger denominator;

        Fraction(BigInteger numerator,
                 BigInteger denominator) {

            if (denominator.signum() == 0) {
                throw new ArithmeticException(
                        "Division by zero");
            }

            if (denominator.signum() < 0) {
                numerator = numerator.negate();
                denominator = denominator.negate();
            }

            BigInteger gcd =
                    numerator.gcd(denominator);

            this.numerator =
                    numerator.divide(gcd);

            this.denominator =
                    denominator.divide(gcd);
        }


        Fraction add(Fraction other) {

            BigInteger newNumerator =
                    numerator.multiply(
                            other.denominator)
                    .add(
                            other.numerator.multiply(
                                    denominator));

            BigInteger newDenominator =
                    denominator.multiply(
                            other.denominator);

            return new Fraction(
                    newNumerator,
                    newDenominator);
        }


        Fraction multiply(Fraction other) {

            BigInteger newNumerator =
                    numerator.multiply(
                            other.numerator);

            BigInteger newDenominator =
                    denominator.multiply(
                            other.denominator);

            return new Fraction(
                    newNumerator,
                    newDenominator);
        }
    }


    

    public static void main(String[] args) {

        try {


            FileReader reader =
                    new FileReader("testcase.json");

            JsonObject root =
                    JsonParser
                    .parseReader(reader)
                    .getAsJsonObject();

            reader.close();



            JsonObject keys =
                    root.getAsJsonObject("keys");

            int n =
                    keys.get("n").getAsInt();

            int k =
                    keys.get("k").getAsInt();

            System.out.println("n = " + n);
            System.out.println("k = " + k);


            

            List<Point> points =
                    new ArrayList<>();

            for (int i = 1; i <= n; i++) {

                String key =
                        String.valueOf(i);

                JsonObject obj =
                        root.getAsJsonObject(key);

                int base =
                        obj.get("base")
                           .getAsInt();

                String value =
                        obj.get("value")
                           .getAsString();


                BigInteger x =
                        BigInteger.valueOf(i);


               
                BigInteger y =
                        new BigInteger(
                                value,
                                base);


                points.add(
                        new Point(x, y));


                System.out.println(
                        "Point: (" +
                        x + ", " +
                        y + ")"
                );
            }


         

            List<Point> selected =
                    points.subList(0, k);


            System.out.println(
                    "\nUsing " +
                    k +
                    " points..."
            );


         

            Fraction answer =
                    lagrangeAtZero(selected);


            System.out.println(
                    "\nConstant coefficient P(0):"
            );

            System.out.println(
                    answer.numerator
            );


            if (!answer.denominator.equals(
                    BigInteger.ONE)) {

                System.out.println(
                        "Fraction = " +
                        answer.numerator +
                        "/" +
                        answer.denominator
                );
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }



    static Fraction lagrangeAtZero(
            List<Point> points) {

        Fraction result =
                new Fraction(
                        BigInteger.ZERO,
                        BigInteger.ONE);


        int k =
                points.size();


       
        for (int i = 0; i < k; i++) {

            Point current =
                    points.get(i);


           
            Fraction term =
                    new Fraction(
                            current.y,
                            BigInteger.ONE);


         
            for (int j = 0; j < k; j++) {

                if (i == j) {
                    continue;
                }


                Point other =
                        points.get(j);


                BigInteger numerator =
                        other.x.negate();


                BigInteger denominator =
                        current.x.subtract(
                                other.x);


                Fraction factor =
                        new Fraction(
                                numerator,
                                denominator);


                term =
                        term.multiply(
                                factor);
            }


           
            result =
                    result.add(term);
        }


        return result;
    }
}