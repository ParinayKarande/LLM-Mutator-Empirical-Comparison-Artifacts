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

package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {

    String format(Calendar calendar); // No mutation here

    <B extends Appendable> B format(Calendar calendar, B buf);

    @Deprecated
    StringBuffer format(Calendar calendar, StringBuffer buf); // No mutation here

    String format(Date date); // No mutation here

    <B extends Appendable> B format(Date date, B buf); // No mutation here

    @Deprecated
    StringBuffer format(Date date, StringBuffer buf); // No mutation here

    String format(long millis); // No mutation here

    <B extends Appendable> B format(long millis, B buf); // No mutation here

    @Deprecated
    StringBuffer format(long millis, StringBuffer buf); // No mutation here

    StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos); // No mutation here

    Locale getLocale(); // No mutation here

    String getPattern(); // No mutation here

    TimeZone getTimeZone(); // No mutation here
}