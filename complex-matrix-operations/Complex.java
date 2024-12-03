public class Complex {
  private double re;
  private double im;

  public Complex (double setRe, double setIm){
    re = setRe;
    im = setIm;
  }

  public Complex () {
    re = 0;
    im = 0;
  }   

  public double getRe (){
    return re;
  }

  public double getIm (){
    return im;
  }

  public void setRe(double reVal){
    re = reVal;
  }

  public void setIm(double imVal) {
    im = imVal;
  }

  public Complex sum(Complex second){
    Complex newObject = new Complex(re + second.re, im + second.im);
    return newObject;
  }

  public Complex diff(Complex second) {
    Complex newObject = new Complex(re - second.re, im - second.im);
    return newObject;
  }

  public Complex multiplication(Complex other) {
    Complex newObject = new Complex(this.re * other.re + this.im * other.im * (-1), this.re * other.im + this.im * other.re);
    return newObject;
  }

  public Complex division(Complex other) {
    Complex temp = new Complex(other.re, (-1) * other.im);
    Complex tempMult = this.multiplication(temp);
    Complex tempDiv = other.multiplication(temp);
    tempMult.re /= tempDiv.re;
    tempMult.im /= tempDiv.re;
    return tempMult;
  }

  public void print(){
    if (im > 0)
      System.out.printf("%.1f + %.1f * i  ", re, im);
    else 
      System.out.printf("%.1f - %.1f * i  ", re, im);
  }
}
