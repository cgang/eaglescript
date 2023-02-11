package org.eaglescript.vm;

import java.io.Serializable;

interface Frame extends Serializable {
    StackTraceElement toStackTrace();
}
