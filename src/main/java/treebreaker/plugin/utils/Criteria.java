/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.utils;

/**
 *
 * @author dsato
 * @param <T>
 */
public interface Criteria<T> {

    public boolean check(T... ts);
}
