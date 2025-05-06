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

package org.apache.commons.lang3.exception;

public class ExceptionUtils {

    public static boolean isEmpty(Throwable throwable) {
        return throwable == null;
    }

    public static String getMessage(Throwable throwable) {
        if (throwable != null) {
            return throwable.getMessage();
        }
        return null;
    }

    public static void printStackTrace(Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    public static String getRootCauseMessage(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = throwable.getCause();
        return (cause != null) ? cause.getMessage() : throwable.getMessage();
    }
}