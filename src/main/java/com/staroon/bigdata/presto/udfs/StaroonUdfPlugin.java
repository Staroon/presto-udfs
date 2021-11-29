/*
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

package com.staroon.bigdata.presto.udfs;

import com.google.common.collect.ImmutableSet;
import com.staroon.bigdata.presto.udfs.scalar.IDCardParseFunction;
import com.staroon.bigdata.presto.udfs.scalar.IPLocationFunction;
import io.prestosql.spi.Plugin;

import java.util.Set;

public class StaroonUdfPlugin
        implements Plugin
{
    @Override
    public Set<Class<?>> getFunctions()
    {
        return ImmutableSet.<Class<?>>builder()
                .add(IPLocationFunction.class)
                .add(IDCardParseFunction.class)
                .build();
    }
}
