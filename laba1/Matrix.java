public class Matrix {
  private Complex[][] matrix;
  private final int row;
  private final int col;

  public Matrix (Pair<Double, Double> arr[][]){
    row = arr.length;
    col = arr[0].length;
    matrix = new Complex[row][col];

    for (int i = 0; i < row; i++){
      int ind = 0;
      for (int j = 0; j < col; j++){
        Complex newOb = new Complex(arr[i][j].getLeft(), arr[i][j].getRight());
        matrix[i][ind++] = newOb;
      }
    }
  }

  public Matrix (int rowNumber, int colNumber){
    this.row = rowNumber;
    this.col = colNumber;
    matrix = new Complex[this.row][this.col];

    // fill array 0 + 0*i elements
    for (int i = 0; i < this.row; i++){
      for (int j = 0; j < this.col; j++){
        Complex obj = new Complex();
        matrix[i][j] = obj;
      }
    }
  }

  public double getElRe(int colNumber, int rowNumber){
    return matrix[colNumber][rowNumber].getRe();
  }

  public double getElIm(int colNumber, int rowNumber){
    return matrix[colNumber][rowNumber].getIm();
  }

  public void setElRe(int colNumber, int rowNumber, int reVal){
    matrix[colNumber][rowNumber].setRe(reVal);
  }

  public void setElIm(int colNumber, int rowNumber, int imVal){
    matrix[colNumber][rowNumber].setIm(imVal);
  }

  public int getCol(){
    return this.col;
  }

  public int getRow(){
    return this.row;
  } 

  public Matrix transposition(){
    Matrix newMatrix = new Matrix(this.col, this.row);

    for (int x = 0; x < this.row; x++){
      for (int y = 0; y < this.col; y++){
        newMatrix.matrix[y][x] = this.matrix[x][y];
      }
    }

    matrix = null;
    return newMatrix;
  }

  public Matrix sum(Matrix another){
    if (another.col == this.col && another.row == this.row){
      Matrix newMatrix = new Matrix(this.row, this.col);
      for (int y = 0; y < this.row; y++){
        for (int x = 0; x < this.col; x++){
          // sum of pair
          newMatrix.matrix[y][x] = another.matrix[y][x].sum(this.matrix[y][x]);
        }
      }
      return newMatrix;
    } else {
      System.out.println("The matrix should be of a different size, match the appropriate size of the sum matrix");
      Matrix newMatrix = new Matrix(1,1);
      return newMatrix;
    }
  }

  public Matrix diff(Matrix another){
    if (another.col == this.col && another.row == this.row){
      Matrix newMatrix = new Matrix(this.row, this.col);
      for (int y = 0; y < this.row; y++){
        for (int x = 0; x < this.col; x++){
          // diff of pair
          newMatrix.matrix[y][x] = another.matrix[y][x].diff(this.matrix[y][x]);
        }
      }
      return newMatrix;
    } else {
      System.out.println("The matrix should be of a different size, match the appropriate size of the diff matrix");
      Matrix newMatrix = new Matrix(1,1);
      return newMatrix;
    }
  }

  public Matrix mult(Matrix another){
    if (another.row == this.col){
      Matrix newMatrix = new Matrix(this.row, this.col);
      for (int y = 0; y < this.row; y++){
        for (int x = 0; x < another.col; x++){
          // mult of pair
          for (int g = 0; g < this.col; g++)
            // +=
            newMatrix.matrix[y][x] = newMatrix.matrix[y][x].sum(this.matrix[y][g].multiplication(another.matrix[g][x]));
        }
      }
      return newMatrix;
    } else {
      System.out.println("The matrix should be of a different size, match the appropriate size of the multiplied matrix");
      Matrix newMatrix = new Matrix(1,1);
      return newMatrix;
    }
  }

  public Matrix multNum(Complex Num){
    Matrix newMatrix = new Matrix(this.row, this.col);
    for (int y = 0; y < this.row; y++){
      for (int x = 0; x < this.row; x++){
        newMatrix.matrix[y][x] = this.matrix[y][x].multiplication(Num);
      }
    }
    return newMatrix;
  }

  public Matrix calculateMinor(Matrix matrix, int rowIndex, int colIndex){
    Matrix temp = new Matrix(matrix.col - 1, matrix.row - 1);

    int ind1 = 0;
    for (int y = 0; y < matrix.row; y++){
      if (y == rowIndex)
        continue;
      
      int ind2 = 0;
      for (int x = 0; x < matrix.col; x++){
        if (x == colIndex)
          continue;
        
        temp.matrix[ind1][ind2] = matrix.matrix[y][x];
        ind2++;
      }
      ind1++;
    }
    return temp;
  }

  public Complex determinant(){
    if (this.col != this.row){
      System.out.println("You can't calculate determinant, the number of col != number of row");
      Complex temp = new Complex(0, 0);
      return temp;
    }

    if (this.col == 1){
      return this.matrix[0][0];
    }

    Complex res = new Complex();
    for (int i = 0; i < this.col; i++){
      Matrix temp = calculateMinor(this, i, 0);
      Complex cur = temp.determinant().multiplication(this.matrix[0][i]);

      if (i % 2 != 0){
        Complex temp2 = new Complex(-1, 0);
        res = res.sum(cur.multiplication(temp2));
      } else {
        res = res.sum(cur);
      }
    }
    return res;
  }

  public void PrintMatrix(){
    for (int y = 0; y < this.row; y++){
      for (int x = 0; x < this.col; x++){
        this.matrix[y][x].print();
      }
      System.out.println("|");
    }
  }
}