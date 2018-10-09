package com.anrisoftware.sscontrol.types.misc.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
public class GeneticListPropertyUtil {

    /**
     *
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface GeneticListProperty<T> {

        void add(T property);
    }

    /**
     * Provides the list property statement.
     *
     * <pre>
     * property << obj
     * </pre>
     */
    @SuppressWarnings("serial")
    public static <T> List<T> geneticListStatement(
            final GeneticListProperty<T> property) {
        return new ArrayList<T>() {

            @Override
            public boolean add(T e) {
                property.add(e);
                return true;
            }

            @Override
            public void add(int index, T element) {
                property.add(element);
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                for (T string : c) {
                    add(string);
                }
                return true;
            }

            @Override
            public boolean addAll(int index, Collection<? extends T> c) {
                for (T string : c) {
                    add(string);
                }
                return true;
            }
        };
    }

}
