package com._4paradigm.fesql_auto_test.performance;

import com._4paradigm.sql.jdbc.SQLResultSet;
import com._4paradigm.sql.sdk.SdkOption;
import com._4paradigm.sql.sdk.SqlException;
import com._4paradigm.sql.sdk.SqlExecutor;
import com._4paradigm.sql.sdk.impl.SqlClusterExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageProcedureExample extends BaseExample {

    private static final Logger logger = LoggerFactory.getLogger(StorageProcedureExample.class);

    private String ddl = "create table trans(c1 string,\n" +
            "                   c3 int,\n" +
            "                   c4 bigint,\n" +
            "                   c5 float,\n" +
            "                   c6 double,\n" +
            "                   c7 timestamp,\n" +
            "                   c8 date,\n" +
            "                   index(key=c1, ts=c7));";

    private SqlExecutor sqlExecutor = null;
    private String db = "test_db2";
    private String tname = "trans";
    private String spName = "sp" + Math.abs(new Random().nextInt());
    private String dropDdl = "drop table trans;";
    private String cardNo = "card1";
    private String merchantId = "merChantId1";

    public void printSQL() {
        logger.info("ddl: \n{}", ddl);
    }

    public void init() throws SqlException {
        SdkOption option = new SdkOption();
        option.setZkCluster(zkCluster);
        option.setZkPath(zkPath);
        option.setSessionTimeout(10000);
        sqlExecutor = new SqlClusterExecutor(option);
    }

    public void initDDL() throws Exception {
        sqlExecutor.createDB(db);
        sqlExecutor.executeDDL(db, dropDdl);
        sqlExecutor.executeDDL(db, ddl);
    }

    public void initSample() {
        sqlExecutor.executeInsert(db, "insert into trans values(\"aa\",20,30,1.1,2.1,1590738990000,\"2020-05-01\");");
        sqlExecutor.executeInsert(db, "insert into trans values(\"aa\",21,31,1.2,2.2,1590738991000,\"2020-05-02\");");
        sqlExecutor.executeInsert(db, "insert into trans values(\"aa\",22,32,1.3,2.3,1590738992000,\"2020-05-03\");");
        sqlExecutor.executeInsert(db, "insert into trans values(\"aa\",23,33,1.4,2.4,1590738993000,\"2020-05-04\");");
//        sqlExecutor.executeInsert(db, "insert into trans values(\"bb\",24,34,1.5,2.5,1590738994000,\"2020-05-05\");");
    }

    public void clearDDL() throws Exception {
        sqlExecutor.executeDDL(db, dropDdl);
    }

    public void createProcedure() throws Exception {
        String sql = "SELECT c1, c3, sum(c4) OVER w1 as w1_c4_sum FROM trans WINDOW w1 AS (PARTITION BY trans.c1 ORDER BY trans.c7 ROWS BETWEEN 2 PRECEDING AND CURRENT ROW);";
        String spSql = "create procedure " + spName + "(" + "const c1 string, const c3 int, c4 bigint, c5 float, c6 double, c7 timestamp, c8 date" + ")" +
                " begin " + sql + " end;";
        boolean ok = sqlExecutor.executeDDL(db, spSql);
        Assert.assertTrue(ok);
    }

    public void callProcedure() throws Exception {
        List<List<Object>> listList = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        list.add("bb");
        list.add(24);
        list.add(34l);
        list.add(1.5f);
        list.add(2.5);
        list.add(new Timestamp(1590738994000l));
        list.add(Date.valueOf("2020-05-05"));
        listList.add(list);

        SQLResultSet sqlResultSet = sqlExecutor.callProcedure(db, spName, listList);
        if (sqlResultSet == null) {
            Assert.fail();
        }
        Assert.assertEquals(sqlResultSet.getMetaData().getColumnCount(), 3);
        sqlResultSet.next();
        Assert.assertEquals(sqlResultSet.getString(1), "bb");
        Assert.assertEquals(sqlResultSet.getInt(2), 24);
        Assert.assertEquals(sqlResultSet.getLong(3), 34);
        sqlResultSet.close();
    }

    public static void run() {
        final StorageProcedureExample example = new StorageProcedureExample();
        try {
            example.init();
            example.initDDL();
            example.initSample();
            example.createProcedure();
            example.callProcedure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        run();
    }
}