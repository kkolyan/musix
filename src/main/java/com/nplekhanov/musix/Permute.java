package com.nplekhanov.musix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Permute{
    public static <T> void permute(List<T> list, int k, Consumer<List<T>> resultHandler){
        for(int i = k; i < list.size(); i++){
            Collections.swap(list, i, k);
            permute(list, k+1, resultHandler);
            Collections.swap(list, k, i);
        }
        if (k == list.size() -1){
            resultHandler.accept(new ArrayList<>(list));
        }
    }
    public static void main(String[] args){
        Permute.permute(java.util.Arrays.asList(3,4,6,2,1), 0, System.out::println);
    }
}