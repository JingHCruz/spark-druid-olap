/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sparklinedata.druid.client.test

import org.apache.spark.sql.hive.test.sparklinedata.TestHive._


class HLLTest extends QueryExtTest {

  test("basicHLL",
    "select approx_count_distinct(city)  " +
      "from zipCodes",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _)
  )

  test("basicApproxCardinality",
    "select approx_count_distinct(zip_code)  " +
      "from zipCodes",
    1,
    true,
    true,
    false,
    Seq(hasCardinalityAgg _)
  )

  test("hllWithFilter",
    "select approx_count_distinct(city)  " +
      "from zipCodes where state = 'NY'",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _)
  )

  test("hllWithJSFilter",
    "select approx_count_distinct(city)  " +
      "from zipCodes where substring(state,1,1) = 'N'",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _)
  )

  test("hllWithJSFilterOnFull",
    "select approx_count_distinct(city)  " +
      "from zipCodesFull where substring(state,1,1) = 'N'",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _)
  )

  test("hllSelect",
    "select city  " +
      "from zipCodes where substring(state,1,1) = 'N'",
    1,
    true,
    true,
    false
  )

  test("spatialSelect",
    "select latitude, longitude  " +
      "from zipCodes " +
      "where latitude > 42.5 and longitude is not null " +
      "limit 10000",
    1,
    true,
    true,
    false,
    Seq(hasSpatialFilter _)
  )

  test("spatialFilOnAgg",
    "select approx_count_distinct(city)  " +
      "from zipCodes " +
      "where latitude > 42.5 and longitude is not null ",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _, hasSpatialFilter _)
  )

  test("combineSpatialFilter1",
    "select approx_count_distinct(city)  " +
      "from zipCodes " +
      "where latitude > 0 and longitude is not null " +
      "and latitude < 18 and longitude > -80 and longitude < 10",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _, hasSpatialFilter _)
  )

  test("combineSpatialFilter2",
    "select approx_count_distinct(city)  " +
      "from zipCodes " +
      "where latitude > 0 and longitude is not null " +
      "and latitude < 18 or (longitude > -80 and longitude < 10)",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _, hasSpatialFilter _)
  )

  test("combineSpatialFilter3",
    "select approx_count_distinct(city)  " +
      "from zipCodes " +
      "where latitude > 0 and substring(state,1,1) = 'N' " +
      "and latitude < 18 and (longitude > -80 and longitude < 10)",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _, hasSpatialFilter _)
  )

  test("combineSpatialFilter4",
    "select approx_count_distinct(city)  " +
      "from zipCodes " +
      "where latitude > 0 and substring(state,1,1) = 'N' " +
      "and latitude < 18 or (longitude > -80 and longitude < 10)",
    1,
    true,
    true,
    false,
    Seq(hasHLLAgg _, hasSpatialFilter _)
  )

}
