public class index {
  public static void main(String[] args) {
    // Working with complex numbers
    Complex num1 = new Complex(4, 3);
    Complex num2 = new Complex(2, -5);

    // Product
    Complex product = num1.multiplication(num2);
    System.out.printf("num1 * num2 = ");
    product.print();
    System.out.println();

    // Division
    Complex quotient = num1.division(num2);
    System.out.printf("num1 / num2 = ");
    quotient.print();
    System.out.println();

    // Sum
    Complex sum = num1.sum(num2);
    System.out.printf("num1 + num2 = ");
    sum.print();
    System.out.println();

    // Difference
    Complex difference = num1.diff(num2);
    System.out.printf("num1 - num2 = ");
    difference.print();
    System.out.println();

    System.out.println();

    // Working with complex matrices
    @SuppressWarnings("unchecked")
    Pair<Double, Double>[][] matrix1 = (Pair<Double, Double>[][]) new Pair[3][2];
    matrix1[0][0] = new Pair<>(2.0, 5.0);
    matrix1[0][1] = new Pair<>(0.0, 4.0);
    matrix1[1][0] = new Pair<>(1.0, 3.0);
    matrix1[1][1] = new Pair<>(4.0, 1.0);
    matrix1[2][0] = new Pair<>(3.0, 1.0);
    matrix1[2][1] = new Pair<>(2.0, 0.0);

    @SuppressWarnings("unchecked")
    Pair<Double, Double>[][] matrix2 = (Pair<Double, Double>[][]) new Pair[3][2];
    matrix2[0][0] = new Pair<>(1.0, -1.0);
    matrix2[0][1] = new Pair<>(2.0, 3.0);
    matrix2[1][0] = new Pair<>(4.0, 5.0);
    matrix2[1][1] = new Pair<>(1.0, 0.0);
    matrix2[2][0] = new Pair<>(-2.0, 0.0);
    matrix2[2][1] = new Pair<>(6.0, -1.0);

    // Create Matrix objects
    Matrix complexMatrix1 = new Matrix(matrix1);
    Matrix complexMatrix2 = new Matrix(matrix2);

    // Sum of matrices
    Matrix matrixSum = complexMatrix1.sum(complexMatrix2);
    System.out.println("matrix1 + matrix2 = ");
    matrixSum.PrintMatrix();
    System.out.println();

    // Difference of matrices
    Matrix matrixDiff = complexMatrix1.diff(complexMatrix2);
    System.out.println("matrix1 - matrix2 = ");
    matrixDiff.PrintMatrix();
    System.out.println();

    // Multiply two matrices
    Matrix matrixProduct = complexMatrix1.mult(complexMatrix2);
    System.out.println("matrix1 * matrix2 = ");
    matrixProduct.PrintMatrix();
    System.out.println();

    // Transpose matrix1
    complexMatrix1 = complexMatrix1.transposition();
    System.out.println("Transposed matrix1: ");
    complexMatrix1.PrintMatrix();
    System.out.println();

    // Find determinant of the matrix
    Complex determinantMatrix1 = complexMatrix1.determinant();
    System.out.printf("det(matrix1) = ");
    determinantMatrix1.print();
    System.out.println();
  }
}
