package com.iust.onlineschool.enumaration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iust.onlineschool.utility.enumaration.EnumSerializer;
import com.iust.onlineschool.utility.enumaration.GradeDeserializer;


/**
 * Created by Mohsen on 11/7/2016.
 */
@JsonSerialize(using = EnumSerializer.class)
@JsonDeserialize(using = GradeDeserializer.class)
public enum Grade {
    bachelor,
    master,
    phd
}
