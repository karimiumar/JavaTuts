package com.umar.apps.hibernate.common;

import java.io.Serializable;

public interface WorkItem<ID extends Serializable> {
    ID getId();
}

