package Price;

public class PriceFactory {
    PriceFactory(){}
   public static Price Build(int cents) {
      return new Price(cents);
   }
}
