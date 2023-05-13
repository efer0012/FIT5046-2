package com.example.befit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.befit.dao.CustomerDao;
import com.example.befit.database.AppDatabase;
import com.example.befit.entity.Customer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CustomerRepository {
    private CustomerDao customerDao;
    private LiveData<List<Customer>> allLiveCustomers;
    private List<Customer> allCustomers;

    public CustomerRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        customerDao = db.customerDao();
        allLiveCustomers = customerDao.getAllCustomers();
        allCustomers = customerDao.getAll();
    }

    // Room executes this query on a separate thread
    public LiveData<List<Customer>> getAllCustomers() {
        return allLiveCustomers;
    }

    public List<Customer> getAll() {
        return allCustomers;
    }

    public void insertCustomer(final Customer customer){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.insert(customer);
            }
        });
    }

    public void deleteAllCustomers(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.deleteAll();
            }
        });
    }

    public void deleteCustomer(final Customer customer){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.delete(customer);
            }
        });
    }

    public void updateCustomer(final Customer customer){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.update(customer);
            }
        });
    }

    public Customer getCustomerByUid(String uid) {
        return customerDao.getCustomerById(uid);
    }

    public Customer getCustomerByEmail(String email){
        return customerDao.findCustomer(email);
    }

    public int getTotalCustomer(){
        return customerDao.getTotalCustomer();
    }

    public CompletableFuture<Customer> findCustomerFuture(final String email) {
        return CompletableFuture.supplyAsync(new Supplier<Customer>() {
            @Override
            public Customer get() {
                return customerDao.findCustomer(email);
            }
        }, AppDatabase.databaseWriteExecutor);
    }
}
