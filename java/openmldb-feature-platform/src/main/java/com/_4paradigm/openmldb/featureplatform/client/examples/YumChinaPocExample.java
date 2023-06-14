package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;

import java.io.IOException;

public class YumChinaPocExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS yumchina");
            client.executeSql("CREATE TABLE IF NOT EXISTS yumchina.request (usercode string, adid string, ts timestamp)");
            client.executeSql("CREATE TABLE IF NOT EXISTS yumchina.ai_place_coupon (usercode string, activityid string, channelid string, starttime timestamp)");
            client.executeSql("CREATE TABLE IF NOT EXISTS yumchina.ods_ai_traffic_kfc (user_id string, adid string, guid string, positioinid string, event_time timestamp)");
            client.executeSql("CREATE TABLE IF NOT EXISTS yumchina.ai_material_tags (materialid string, communicationtype string, communicatioingoal string, daypart string)");

            // Create entity
            client.createEntity("usercode", "usercode");

            // Create feature view
            String sql = "select usercode,\n" +
                    "count(activityid) over w1 as coupon_cnt,\n" +
                    "count_cate(activityid, channelid) over w1 as coupon_channel_cnt\n" +
                    "from\n" +
                    "(select usercode, \"\" as activityid, \"\" as channelid, ts as starttime from request) as t1\n" +
                    "window w1 as (union (select usercode, activityid, channelid, starttime from ai_place_coupon) as t2\n" +
                    "partition by usercode order by starttime\n" +
                    "rows_range between 7d preceding and 1 PRECEDING instance_not_in_window)";

            client.createFeatureView("user_coupon", "usercode", "yumchina", sql);
            /*
            client.createFeatureView("featureview2", "name", "SELECT name, age, amount FROM test_db.user LAST JOIN test_db.trade ON test_db.user.name = test_db.trade.user_name");

            // Create feature service
            client.createFeatureService("featureservice1", "featureview1, featureview2");

            // Test feature service
            client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

            // Cleanup resources
            client.deleteFeatureService("featureservice1");
            client.deleteFeatureView("featureview1");
            client.deleteFeatureView("featureview2");
            client.deleteEntity("name");
            client.executeSql("DROP TABLE test_db.user");
            client.executeSql("DROP TABLE test_db.trade");
            client.executeSql("DROP DATABASE test_db");
            */
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
