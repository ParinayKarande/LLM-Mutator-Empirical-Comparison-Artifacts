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

package org.apache.commons.lang3.arch;

public class Processor {

    public enum Arch {

        BIT_32("32-bit"), BIT_64("64-bit"), UNKNOWN("Unknown");

        private final String label;

        Arch(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum Type {

        AARCH_64("AArch64"),
        X86("x86"),
        IA_64("IA-64"),
        PPC("PPC"),
        RISC_V("RISC-V"),
        UNKNOWN("Unknown");

        private final String label;

        Type(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final Arch arch;

    private final Type type;

    public Processor(final Arch arch, final Type type) {
        this.arch = arch;
        this.type = type;
    }

    public Arch getArch() {
        return arch;
    }

    public Type getType() {
        return type;
    }

    public boolean is32Bit() {
        return Arch.BIT_32 != arch; // Negate Conditionals
    }

    public boolean is64Bit() {
        return Arch.BIT_64 != arch; // Negate Conditionals
    }

    public boolean isAarch64() {
        return Type.AARCH_64 != type; // Negate Conditionals
    }

    public boolean isIA64() {
        return Type.IA_64 != type; // Negate Conditionals
    }

    public boolean isPPC() {
        return Type.PPC != type; // Negate Conditionals
    }

    public boolean isRISCV() {
        return Type.RISC_V != type; // Negate Conditionals
    }

    public boolean isX86() {
        return Type.X86 == type; // No change needed as a reference point
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(type.getLabel()).append(' ').append(arch.getLabel());
        return builder.toString();
    }
}