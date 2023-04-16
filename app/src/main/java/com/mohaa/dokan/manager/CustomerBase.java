package com.mohaa.dokan.manager;

import com.mohaa.dokan.models.wp.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerBase {

    private List<Customer> mOrders;
    private static volatile CustomerBase instance = new CustomerBase();
    private CustomerBase(){
        mOrders = new ArrayList<>();
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    private CustomerBase(Customer products)
    {
        mOrders.add(products);
    }

    public List<Customer> getmOrders() {
        return mOrders;
    }

    public boolean InsertOrder(Customer products) {

        this.mOrders.add(0, products);
        return true;

    }
    public Customer CheckAvaliablty(int product_id) {

        if (mOrders.size() > 0) {

            for (int i = 0; i < mOrders.size(); i++) {
                if(mOrders.get(i).getId() == product_id)
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

    public boolean RemoveOrder(Customer products) {

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

    public static CustomerBase getInstance(){
        if(instance == null)
        {
            instance = new CustomerBase();
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
