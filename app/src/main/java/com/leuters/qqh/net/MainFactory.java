package com.leuters.qqh.net;

public class MainFactory {

    public static LoanService loanService;
    protected static final Object monitor = new Object();

    public static LoanService getComicServiceInstance(){
        synchronized (monitor){
            if(loanService ==null){
                loanService = new MainRetrofit().getService();
            }
            return loanService;
        }
    }
}
