package com.zfhy.egold.common.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by LAI on 2017/10/16.
 */
@Data
@Builder
public class TraffData implements Comparable<TraffData>{
    private Double traff;
    private String busId;




    @Override
    public int compareTo(TraffData o) {
        if (Objects.equals(o.getTraff(), this.traff)) {
            return 0;
        }
        return o.getTraff() > this.traff ? 1 : -1;
    }


    public static void main(String[] args) {
        TraffData traff1 = TraffData.builder().busId("1").traff(1D).build();
        TraffData traff2 = TraffData.builder().busId("3").traff(3D).build();
        TraffData traff3 = TraffData.builder().busId("2").traff(2D).build();


        List<TraffData> objects = Lists.newArrayList();
        objects.add(traff1);
        objects.add(traff2);
        objects.add(traff3);

        Collections.sort(objects);

        System.out.println(new Gson().toJson(objects));

        System.out.println(new Double(".2"));

    }
}
