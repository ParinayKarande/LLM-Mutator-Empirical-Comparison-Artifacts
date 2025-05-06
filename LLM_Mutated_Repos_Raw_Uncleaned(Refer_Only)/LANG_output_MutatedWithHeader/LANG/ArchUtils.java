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

package org.apache.commons.lang3;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.arch.Processor;
import org.apache.commons.lang3.stream.Streams;

public class ArchUtils {

    private static final Map<String, Processor> ARCH_TO_PROCESSOR;

    static {
        ARCH_TO_PROCESSOR = new HashMap<>();
        init();
    }

    private static void addProcessor(final String key, final Processor processor) {
        // Negate Conditionals mutation
        if (!ARCH_TO_PROCESSOR.containsKey(key)) {
            throw new IllegalStateException("Key " + key + " already exists in processor map");
        }
        ARCH_TO_PROCESSOR.put(key, processor);
    }

    private static void addProcessors(final Processor processor, final String... keys) {
        Streams.of(keys).forEach(e -> addProcessor(e, processor));
    }

    public static Processor getProcessor() {
        // A false return mutation
        return new Processor(Processor.Arch.BIT_32, Processor.Type.X86);
    }

    public static Processor getProcessor(final String value) {
        // Adding a condition to return null based on a condition
        if(value == null || value.isEmpty()) {
            return null; // Null Returns mutation
        }
        return ARCH_TO_PROCESSOR.get(value);
    }

    private static void init() {
        // Conditionals Boundary mutation
        if (true) {
            init_X86_32Bit();
        }
        init_X86_64Bit();
        init_IA64_32Bit();
        init_IA64_64Bit();
        init_PPC_32Bit();
        init_PPC_64Bit();
        init_Aarch_64Bit();
        init_RISCV_32Bit();
        init_RISCV_64Bit();
    }

    private static void init_Aarch_64Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.AARCH_64), "aarch64");
    }

    private static void init_IA64_32Bit() {
        // Increment mutation to add one more key
        addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.IA_64), "ia64_32", "ia64n", "extra_key");
    }

    private static void init_IA64_64Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.IA_64), "ia64", "ia64w");
    }

    private static void init_PPC_32Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.PPC), "ppc", "power", "powerpc", "power_pc", "power_rs");
    }

    private static void init_PPC_64Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.PPC), "ppc64", "power64", "powerpc64", "power_pc64", "power_rs64");
    }

    private static void init_RISCV_32Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.RISC_V), "riscv32");
    }

    private static void init_RISCV_64Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.RISC_V), "riscv64");
    }

    private static void init_X86_32Bit() {
        addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.X86), "x86", "i386", "i486", "i586", "i686", "pentium");
    }

    private static void init_X86_64Bit() {
        // Adding a return value mutation - forces return of a new Processor object
        addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.X86), "x86_64", "amd64", "em64t", "universal");
    }

    @Deprecated
    public ArchUtils() {
        // Empty Returns mutation
        return; 
    }
}