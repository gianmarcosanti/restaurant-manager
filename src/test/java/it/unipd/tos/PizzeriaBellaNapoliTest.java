package it.unipd.tos;

import it.unipd.tos.business.exception.RestaurantBillException;
import it.unipd.tos.model.ItemType;
import it.unipd.tos.model.MenuItem;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class PizzeriaBellaNapoliTest {

    private List<MenuItem> getItemsOrdered() {
        return Stream.of(
                new MenuItem(ItemType.PIZZE, "pizza1", 3),
                new MenuItem(ItemType.PIZZE, "pizza2", 4),
                new MenuItem(ItemType.PIZZE, "pizza3", 5),
                new MenuItem(ItemType.PIZZE, "pizza4", 6),
                new MenuItem(ItemType.PIZZE, "pizza5", 7),
                new MenuItem(ItemType.PIZZE, "pizza6", 8),
                new MenuItem(ItemType.PIZZE, "pizza7", 9),
                new MenuItem(ItemType.PRIMI, "primo1", 10),
                new MenuItem(ItemType.PRIMI, "primo2", 11),
                new MenuItem(ItemType.PRIMI, "primo3", 12),
                new MenuItem(ItemType.PRIMI, "primo4", 13),
                new MenuItem(ItemType.PRIMI, "primo5", 11)
        ).collect(Collectors.toList());
    }

    private static double getTotal(List<MenuItem> itemsOrdered) {
        return itemsOrdered
                .stream()
                .mapToDouble(m -> m.getPrice())
                .sum();
    }
    @Test
    public void testItemTypeEnumValues() {
        assertEquals("PIZZE", ItemType.valueOf(ItemType.PIZZE.toString()).name());
        assertEquals("PRIMI", ItemType.valueOf(ItemType.PRIMI.toString()).name());
    }

    /**
     * Dato un elenco di ordinazioni (Pizze e Primi piatti) calcolare il totale (somma del prezzo dei
     * prodotti ordinati)
     */
    @Test
    public void testGetOrderPriceBaseCase() throws RestaurantBillException {
        it.unipd.tos.PizzeriaBellaNapoli bill = new it.unipd.tos.PizzeriaBellaNapoli();
        List<MenuItem> itemsOrdered = getItemsOrdered();
        assertEquals( getTotal(itemsOrdered), bill.getOrderPrice(itemsOrdered), 0.001);
    }

    /**
     * Se l’importo totale delle ordinazioni (Pizze e Primi) supera i 100 euro viene fatto un 5% di
     * sconto
     */
    @Test
    public void testGetOrderPrice5PercentDiscountIfMoreThan100Euros() throws RestaurantBillException {
        PizzeriaBellaNapoli bill = new PizzeriaBellaNapoli();
        List<MenuItem> itemsOrdered = getItemsOrdered();
        itemsOrdered.add(new MenuItem(ItemType.PIZZE, "Oro e argento", 100.00));
        double rawPrice = getTotal(itemsOrdered);
        double discount = rawPrice * 5 / 100;
        assertEquals(bill.getOrderPrice(itemsOrdered), rawPrice - discount, 0.001);
    }

    /**
     * Non è possibile avere un’ordinazione con più di 20 elementi (se accade prevedere un messaggio
     * d’errore
     */
    @Test(expected = RestaurantBillException.class)
    public void testGetOrderPriceNoMoreThan20Items() throws RestaurantBillException {
        PizzeriaBellaNapoli bill = new PizzeriaBellaNapoli();
        List<MenuItem> list = getItemsOrdered();
        list.addAll(list);

        assert(list.size() >= 20);
        bill.getOrderPrice(list);
    }

    /**
     * Se vengono ordinate più di 10 Pizze la meno costosa è in regalo (l’importo totale non considera
     * la pizza meno costosa)
     */
    @Test
    public void testGetOrderPriceCheapestPizzaIsFreeIfMoreThan10PizzasWithTotalPriceMoreThan100() throws RestaurantBillException {
        PizzeriaBellaNapoli bill = new PizzeriaBellaNapoli();
        List<MenuItem> list = getItemsOrdered();
        list.add(new MenuItem(ItemType.PIZZE, "pizza8", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza9", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza10", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza11", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza12", 7));
        double totalRawPrice = getTotal(list);
        double minPrice = 3;
        assertEquals((totalRawPrice - minPrice)*0.95, bill.getOrderPrice(list) , 0.0001);
    }
    @Test
    public void testGetOrderPriceCheapestPizzaIsFreeIfMoreThan10PizzasWithTotalPriceLessThan100() throws RestaurantBillException {
        PizzeriaBellaNapoli bill = new PizzeriaBellaNapoli();
        List<MenuItem> list = getItemsOrdered().stream()
                                                .filter(x -> x.getItemType().toString().equals("PIZZE"))    // tolgo i primi cosiì da avere in totale minore di 100
                                                .collect(Collectors.toList());                              //
        list.add(new MenuItem(ItemType.PIZZE, "pizza8", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza9", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza10", 7));
        list.add(new MenuItem(ItemType.PIZZE, "pizza11", 7));
        double totalRawPrice = getTotal(list);
        double minPrice = 3;
        assertEquals(totalRawPrice - minPrice, bill.getOrderPrice(list) , 0.0001);
    }

    /**
     * Mi aspetto che se la lista di ordinazione è nulla, il prezzo calcolato sia 0.
     * Non deve quindi essere lanciata una NullPointerException
     */
    @Test
    public void testGetOrderPriceNullList() throws RestaurantBillException {
        PizzeriaBellaNapoli bill = new PizzeriaBellaNapoli();
        assertEquals(0, bill.getOrderPrice(null), 0.0001);
    }
}