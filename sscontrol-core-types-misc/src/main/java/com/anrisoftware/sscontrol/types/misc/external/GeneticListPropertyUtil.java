/**
 * Copyright © 2020 Erwin Müller (erwin.mueller@anrisoftware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.sscontrol.types.misc.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
 * @version 1.0
 */
public class GeneticListPropertyUtil {

    /**
     *
     *
     * @author Erwin Müller {@literal <erwin.mueller@deventm.de>}
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
