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
public class StringListPropertyUtil {

    /**
     * 
     *
     * @author Erwin Müller <erwin.mueller@deventm.de>
     * @version 1.0
     */
    public interface ListProperty {

        void add(String property);
    }

    /**
     * Provides the list property statement.
     * 
     * <pre>
     * property << 'name = value'
     * </pre>
     */
    @SuppressWarnings({ "serial", "rawtypes", "unchecked" })
    public static List<String> stringListStatement(
            final ListProperty property) {
        return new ArrayList() {

            @Override
            public boolean add(Object e) {
                property.add(e.toString());
                return true;
            }

            @Override
            public void add(int index, Object element) {
                property.add(element.toString());
            }

            @Override
            public boolean addAll(Collection c) {
                for (Object string : c) {
                    add(string);
                }
                return true;
            }

            @Override
            public boolean addAll(int index, Collection c) {
                for (Object string : c) {
                    add(string);
                }
                return true;
            }
        };
    }

}
