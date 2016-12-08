package com.iust.onlineschool.enumaration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iust.onlineschool.utility.enumaration.EnumSerializer;
import com.iust.onlineschool.utility.enumaration.RoleTypeDeserializer;

/**
 * @author mojtaba khallash
 */
@JsonSerialize(using = EnumSerializer.class)
@JsonDeserialize(using = RoleTypeDeserializer.class)
public enum RoleType {
    teacher,
    student,
    superuser,
    admin,
    guest
}
