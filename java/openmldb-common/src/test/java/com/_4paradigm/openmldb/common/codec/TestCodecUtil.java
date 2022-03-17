/*
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.openmldb.common.codec;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Date;

public class TestCodecUtil {

    @Test
    public void testDateToDateInt() {
        Date date = Date.valueOf("1970-01-02");
        int expectDateInt = 4587522;
        int dateInt = CodecUtil.dateToDateInt(date);
        Assert.assertEquals(expectDateInt, dateInt);
    }

    @Test
    public void daysToDateInt() {
        int days = 1;
        int expectDateInt = 4587522;
        int dateInt = CodecUtil.daysToDateInt(days);
        Assert.assertEquals(expectDateInt, dateInt);
    }

    @Test
    public void testDateIntToDate() {
        int dateInt = 4587522;
        Date expectDate = Date.valueOf("1970-01-02");
        Date date = CodecUtil.dateIntToDate(dateInt);
        Assert.assertEquals(expectDate, date);
    }

    @Test
    public void testDateIntToDays() {
        int dateInt = 4587522;
        int expectDays = 1;
        int days = CodecUtil.dateIntToDays(dateInt);
        Assert.assertEquals(expectDays, days);
    }

    // Test the combination of dateToDateInt and dateIntToDate
    @Test
    public void testFromDateToDate() {
        Date date = Date.valueOf("1970-01-02");
        Date expectDate = Date.valueOf("1970-01-02");
        Date actualDate = CodecUtil.dateIntToDate(CodecUtil.dateToDateInt(date));
        Assert.assertEquals(expectDate, actualDate);
    }

    // Test the combination of daysToDateInt and dateIntToDays
    @Test
    public void testFromDaysToDays() {
        int days = 10;
        int expectDays = 10;
        int actualDays = CodecUtil.dateIntToDays(CodecUtil.daysToDateInt(days));
        Assert.assertEquals(expectDays, actualDays);
    }

}

