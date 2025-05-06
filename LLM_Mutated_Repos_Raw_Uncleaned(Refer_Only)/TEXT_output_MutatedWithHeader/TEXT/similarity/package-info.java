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

package org.apache.commons.text.similarity;

public class LevenshteinDistance {

    public int compute(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0; // Return value
        }
        int n = s1.length();
        int m = s2.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0) {
                    dp[i][j] = j; // Cost of insertion
                } else if (j == 0) {
                    dp[i][j] = i; // Cost of deletion
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[n][m]; // Return value
    }
}