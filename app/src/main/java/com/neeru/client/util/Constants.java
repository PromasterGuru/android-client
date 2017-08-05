package com.neeru.client.util;



import com.neeru.client.models.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by brajendra on 15/07/17.
 */

public class Constants {
    public static final String ISO_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static SimpleDateFormat formatter = new SimpleDateFormat(ISO_DATE_FORMAT);
    public static SimpleDateFormat formatterDate = new SimpleDateFormat("dd-MMM");

    public static final String URL = "https://api.arkraiders.in/";









    public ArrayList<Product> loadProduct() {

        ArrayList<Product> items = new ArrayList<Product>();
        for (int i = 1; i <= 10; i++) {


            Product product = new Product();
            product.name = "Falcon " + i;
            product.id = i + 1;
            product.price = i * 10;

            items.add(product);


        }


        return items;
    }
}
