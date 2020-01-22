package com.zheng.loadbalance;

public interface BalanceUpdateProvider
{
    boolean addBalance(Integer step);

    boolean reduceBalance(Integer step);
}
