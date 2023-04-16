package com.mohaa.dokan.manager;

import com.mohaa.dokan.models.PendingProduct;

import java.util.ArrayList;
import java.util.List;

public class OrdersBase {

    private List<PendingProduct> mOrders;
    private static volatile OrdersBase instance = new OrdersBase();
    private OrdersBase(){
        mOrders = new ArrayList<>();
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    private OrdersBase(PendingProduct products)
    {
        mOrders.add(products);
    }

    public List<PendingProduct> getmOrders() {
        return mOrders;
    }

    public boolean InsertOrder(PendingProduct products) {

        if (mOrders.size() > 0) {
            if (this.mOrders.get(products.getId() - 1).getProduct_id() == (products.getProduct_id())) {
                return false;
            } else {

                this.mOrders.add(products.getId(), products);
                return true;
            }
        }
        else
        {

            this.mOrders.add(products.getId(), products);
            return true;
        }

    }
    public PendingProduct CheckAvaliablty(int product_id) {

        if (mOrders.size() > 0) {

            for (int i = 0; i < mOrders.size(); i++) {
                if(mOrders.get(i).getProduct_id() == product_id)
                {
                    return this.mOrders.get(i);
                }


            }
            return null;

        }
        else {
            return null;
        }

    }

    public boolean RemoveOrder(PendingProduct products) {

        if (mOrders.size() > 0) {


                //1-2-4-5 ID >> index array >>5
                 int index = mOrders.indexOf(products);
                 mOrders.remove(products);
                 /*
                for (int i = 0; i < mOrders.size() ; i++)
                {
                  if(mOrders.contains(products))
                  {
                      this.mOrders.remove(i);

                      break;
                  }

                }

                  */
                return true;

        }
        else
        {



            return true;
        }

    }

    public static OrdersBase getInstance(){
        if(instance == null)
        {
            instance = new OrdersBase();
        }
        return instance;
    }

    public void dispose() {
        clearTempUser();
    }

    private void clearTempUser() {
        mOrders.clear();

    }

}
