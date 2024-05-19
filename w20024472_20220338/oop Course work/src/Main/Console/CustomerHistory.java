package Main.Console;


import java.util.List;

public class CustomerHistory
{
    User user;
    List<Product> productList;
    double TotalCost;

    boolean firstTimePurchesDiscount;

    public List<Product> getProductList()
    {
        return productList;
    }

    public void setProductList( List<Product> productList )
    {
        this.productList = productList;
    }

    public double getTotalCost()
    {
        return TotalCost;
    }

    public void setTotalCost( double totalCost )
    {
        TotalCost = totalCost;
    }


    public User getUser()
    {
        return user;
    }

    public boolean isFirstTimePurchesDiscount()
    {
        return firstTimePurchesDiscount;
    }

    public void setFirstTimePurchesDiscount( boolean firstTimePurchesDiscount )
    {
        this.firstTimePurchesDiscount = firstTimePurchesDiscount;
    }

    public CustomerHistory( User user,List<Product> productList, double totalCost )
    {
        this.user = user;
        this.productList = productList;
        TotalCost = totalCost;
        this.firstTimePurchesDiscount = true;
        ;
    }
}

