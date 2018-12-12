////////////////////////////////////////////////////////////////////
// GIANMARCO SANTI 1143544
////////////////////////////////////////////////////////////////////

package it.unipd.tos.business;

import it.unipd.tos.business.exception.RestaurantBillException;
import it.unipd.tos.model.MenuItem;

import java.util.List;

public interface RestaurantBill {
    double getOrderPrice(List<MenuItem> itemsOrdered) throws RestaurantBillException;
}