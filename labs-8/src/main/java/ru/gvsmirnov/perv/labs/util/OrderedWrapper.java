package ru.gvsmirnov.perv.labs.util;

public class OrderedWrapper<T> implements Comparable<OrderedWrapper<T>> {
    public final long ordinal;
    public final T value;

    public OrderedWrapper(long ordinal, T value) {
        this.ordinal = ordinal;
        this.value = value;
    }

    @Override
    public int compareTo(OrderedWrapper<T> o) {
        return Long.valueOf(ordinal).compareTo(o.ordinal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedWrapper that = (OrderedWrapper) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
