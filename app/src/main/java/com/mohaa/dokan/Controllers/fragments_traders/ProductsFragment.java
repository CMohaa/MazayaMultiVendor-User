package com.mohaa.dokan.Controllers.fragments_traders;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mohaa.dokan.Controllers.activities_products.IndividualProductActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.dokan.Utils.GridSpacingItemDecoration;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.shimmer.ShimmerFrameLayout;
import com.mohaa.dokan.interfaces.MyResultReceiver;
import com.mohaa.dokan.interfaces.OnProductClickListener;
import com.mohaa.dokan.manager.ApiServices.TraderAPIService;
import com.mohaa.dokan.manager.ApiServices.VariantsAPIService;
import com.mohaa.dokan.manager.Call.Variants;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.models.Variant;
import com.mohaa.dokan.views.CartProductsAdapter;
import com.mohaa.dokan.views.FilterItemListAdapter;
import com.mohaa.dokan.views.ProductsWPAdapter;
import com.mohaa.dokan.views.SortItemListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements OnProductClickListener , CartProductsAdapter.CartProductsAdapterListener {


    private static final String TAG = "TraderActivity";
    public ProductsFragment() {

        // Required empty public constructor
    }
    public MyResultReceiver resultreceiver;
    private RecyclerView recList;
    private ArrayList<com.mohaa.dokan.models.wp.Product> products_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private ProductsWPAdapter products_listAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int traderID;
    private int userPage = 1;
    RelativeLayout sort, filter;
    TextView sortByText;
    Map<Integer , String> sortBy = new HashMap<>();
    int sortById = 0;
    List<String> departmentFilter = new ArrayList<>();
    List<String> companyFilter = new ArrayList<>();
    List<String> packFilter = new ArrayList<>();
    List<String> statusFilter = new ArrayList<>();
    private int selectedKey;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (MyResultReceiver)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products , container , false);
        sortBy.put(0 , getResources().getString(R.string.most_recent));
        sortBy.put(1 , getResources().getString(R.string.most_order));
        sortBy.put(2 , getResources().getString(R.string.top_rated));
        sortBy.put(3 , getResources().getString(R.string.most_viewed));
        selectedKey = 0;
        //traderID =  getArguments().getInt("traderID");
        traderID  = resultreceiver.getResult();
        shimmerFrameLayout = view.findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout.startShimmer();
        recList = (RecyclerView) view.findViewById(R.id.recyclerview);
        products_list = new ArrayList<>();

        products_listAdapter = new ProductsWPAdapter(products_list , this , this);

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getData(userPage , sortByText.getText().toString());
            }
        });
        recList.setAdapter(products_listAdapter);


        //
        sort = view.findViewById(R.id.sortLay);
        filter = view.findViewById(R.id.filterLay);
        sortByText = view.findViewById(R.id.sortBy);
        setSortListener();
        setFilterListener();
        sortByText.setText(sortBy.get(selectedKey));
        getData(1 , sortByText.getText().toString());

        // Inflate the layout for this fragment
        return view;
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void getAllPosts(int userPage , String sortById)
    {

        TraderAPIService service = RetrofitApi.retrofitReadWD().create(TraderAPIService.class);


        Call<List<com.mohaa.dokan.models.wp.Product>> call = service.getStoreProducts(traderID , String.valueOf(userPage));

        call.enqueue(new Callback<List<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<List<com.mohaa.dokan.models.wp.Product>> call, Response<List<com.mohaa.dokan.models.wp.Product>> response) {
                if(response.body() != null)
                {
                    Log.d(TAG, "onResponse: Succ VendorProduct ");
                    products_list.addAll(response.body());
                    products_listAdapter.notifyDataSetChanged();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                }
                else {
                    Log.d(TAG, "onResponse: failed VendorProduct ");
                }



            }

            @Override
            public void onFailure(Call<List<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void getAllPosts_Filter(String name ,String value)
    {

    }


    public void getData(int userPage , String _sortById){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllPosts(userPage ,_sortById );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }











    @Override
    public void onProductClicked(com.mohaa.dokan.models.wp.Product contact, int position) {
        //Toast.makeText(this, contact.getProduct_name(), Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(getContext(), IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
       // loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);
    }

    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.sort_listview);

                ListView listView = dialog.findViewById(R.id.sort_listview);

                listView.setAdapter(new SortItemListAdapter(getContext(), sortBy, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);




                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        selectedKey = sortById;
                        sortByText.setText(sortBy.get(sortById));

                        // Reload Products List
                        userPage = 1;
                        products_list.clear();
                        products_listAdapter.notifyDataSetChanged();
                        getData( 1, sortByText.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterlayout);

                final List<String> department = getVarients(1);
                final List<String> company = getVarients(2);
                final List<String> pack = getVarients(3);
                final List<String> status= getVarients(4);

                // Add into hash map
                HashMap<String, List<String>> listHashMap = new HashMap<>();
                listHashMap.put("department", department);
                listHashMap.put("company", company);
                listHashMap.put("pack", pack);
                listHashMap.put("status", status);

                // Add Headers
                List<String> headers = new ArrayList<>();
                headers.add("department");
                headers.add("company");
                headers.add("pack");
                headers.add("status");

                final ExpandableListView listView = dialog.findViewById(R.id.expandableList);
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(getContext(), headers, listHashMap, departmentFilter, companyFilter, packFilter, statusFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case 0: // department
                                if (!departmentFilter.contains(department.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    departmentFilter.add(department.get(childPosition));
                                } else {
                                    departmentFilter.remove(department.get(childPosition));
                                }
                                break;

                            case 1: // occasion
                                if (!companyFilter.contains(company.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    companyFilter.add(company.get(childPosition));
                                } else {
                                    companyFilter.remove(company.get(childPosition));
                                }
                                break;
                            case 2: // material
                                if (!packFilter.contains(pack.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    packFilter.add(pack.get(childPosition));
                                } else {
                                    packFilter.remove(pack.get(childPosition));
                                }
                                break;
                            case 3: // occasion
                                if (!statusFilter.contains(status.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    statusFilter.add(status.get(childPosition));
                                } else {
                                    statusFilter.remove(status.get(childPosition));
                                }
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

                // Filter Apply Button Click
                Button apply = dialog.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (companyFilter.size() > 0) {
                            getAllPosts_Filter("company", companyFilter.get(0));
                        } else if (departmentFilter.size() > 0) {
                            getAllPosts_Filter("department", departmentFilter.get(0));
                        } else if (packFilter.size() > 0) {
                            getAllPosts_Filter("pack", packFilter.get(0));
                        } else if (statusFilter.size() > 0) {
                            getAllPosts_Filter("status", statusFilter.get(0));
                        } else {
                            getData(1, sortByText.getText().toString());
                        }
                        // Reload Products List By Filter
                        // fillGridView();
                        dialog.dismiss();
                    }
                });

                // Clear All Button Click
                Button clear = dialog.findViewById(R.id.clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            departmentFilter.clear();
                        } catch (NullPointerException ignore) {

                        }

                        try {
                            companyFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            packFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            statusFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });

                // Close Button
                final ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private List<String> getNames(List<Variant> type) {
        // Toast.makeText(this, type.size(), Toast.LENGTH_SHORT).show();
        List<String> type_values = new ArrayList<>();
        for (int i = 0; i < type.size(); i++) {
            type_values.add(type.get(i).getName());
            Toast.makeText(getContext(), type.get(i).getName(), Toast.LENGTH_SHORT).show();
        }
        return type_values;
    }

    private List<String> getVarients(int type) {
        final List<String> vairent = new ArrayList<>();
        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);


        Call<Variants> call = service.getVariants(type);

        call.enqueue(new Callback<Variants>() {
            @Override
            public void onResponse(Call<Variants> call, Response<Variants> response) {

                for (int i = 0; i < response.body().getVariants().size(); i++) {
                    vairent.add(response.body().getVariants().get(i).getName());
                    /*
                    if(response.body().getVariants().get(i).getType() == type)
                    {

                        vairent.add(response.body().getVariants().get(i).getName());

                    }

                     */

                }


            }

            @Override
            public void onFailure(Call<Variants> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return vairent;

    }

    @Override
    public void onCartItemRemoved(int index, PendingProduct cartItem) {

    }

    @Override
    public void onCartItemAdded(int index) {

    }



    @Override
    public void onQuantityChnaged(int index) {

    }
}
