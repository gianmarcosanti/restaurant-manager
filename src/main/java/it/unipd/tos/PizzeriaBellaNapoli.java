////////////////////////////////////////////////////////////////////
// GIANMARCO SANTI 1143544
////////////////////////////////////////////////////////////////////

package it.unipd.tos;

import it.unipd.tos.business.RestaurantBill;
import it.unipd.tos.business.exception.RestaurantBillException;
import it.unipd.tos.model.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

public class PizzeriaBellaNapoli implements RestaurantBill
{
    public double getOrderPrice(List<MenuItem> itemsOrdered) throws RestaurantBillException {
        if( itemsOrdered==null) {
            return 0;
        }

        if(itemsOrdered.size() > 20) {
            throw new RestaurantBillException("Troppi elementi in un solo ordine");
        }

        double price = itemsOrdered.stream().mapToDouble(o -> o.getPrice()).sum();

        List<MenuItem> pizzasOrdered = itemsOrdered.stream()
                                                    .filter(x -> x.getItemType().toString().equals("PIZZE"))
                                                    .collect(Collectors.toList());

        if(pizzasOrdered.size() > 10){
            price -=  pizzasOrdered.stream().mapToDouble(o -> o.getPrice()).min().getAsDouble();
        }

        if(price > 100) {
            price *= 0.95;
        }

        return price;
    }
}
