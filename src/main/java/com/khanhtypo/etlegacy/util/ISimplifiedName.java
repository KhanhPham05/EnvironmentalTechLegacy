package com.khanhtypo.etlegacy.util;

import java.util.Locale;

public interface ISimplifiedName {
    default String toSimpleName() {
        return this.toString().toLowerCase(Locale.ROOT);
    }
}
