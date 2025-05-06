/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections4.Transformer;

public final class MapTransformer<T, R> implements Transformer<T, R>, Serializable {

    private static final long serialVersionUID = 862391807045468939L;

    public static <I, O> Transformer<I, O> mapTransformer(final Map<? super I, ? extends O> map) {
        if (map == null || map.isEmpty()) {  // Conditionals Boundary Mutation
            return ConstantTransformer.<I, O>nullTransformer();
        }
        return new MapTransformer<>(map);
    }

    private final Map<? super T, ? extends R> iMap;

    private MapTransformer(final Map<? super T, ? extends R> map) {
        iMap = map;
    }

    public Map<? super T, ? extends R> getMap() {
        return iMap;
    }

    @Override
    public R transform(final T input) {
        return iMap.get(input);
    }
}