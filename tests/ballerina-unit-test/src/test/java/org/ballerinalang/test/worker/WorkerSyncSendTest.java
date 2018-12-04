/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ballerinalang.test.worker;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BError;
import org.ballerinalang.model.values.BValue;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Tests the sync send worker action.
 */
public class WorkerSyncSendTest {

    private CompileResult result;

    @BeforeClass
    public void setup() {

        this.result = BCompileUtil.compile("test-src/workers/sync-send.bal");
        Assert.assertEquals(result.getErrorCount(), 0, Arrays.asList(result.getDiagnostics()).toString());
    }

    @Test
    public void simpleSyncSendTest() {

        BValue[] returns = BRunUtil.invoke(result, "simpleSyncSend");
        Assert.assertTrue(returns[0].stringValue().startsWith("w2w2w2w2w2"),
                "Returned wrong value:" + returns[0].stringValue());
    }

    @Test
    public void multipleSyncSendTest() {

        BValue[] returns = BRunUtil.invoke(result, "multipleSyncSend");
        Assert.assertTrue(returns[0].stringValue().startsWith("w2w2w2w2w2"),
                "Returned wrong value:" + returns[0].stringValue());
        Assert.assertFalse(returns[0].stringValue().startsWith("w11"),
                "Returned wrong value:" + returns[0].stringValue());
    }

    @Test
    public void nilReturnTest() {

        BValue[] returns = BRunUtil.invoke(result, "process2");
        Assert.assertEquals(returns[0], null);
    }

    @Test
    public void multiWorkerTest() {

        BValue[] returns = BRunUtil.invoke(result, "multiWorkerSend");
        Assert.assertFalse(returns[0].stringValue().startsWith("w1"),
                "Returned wrong value:" + returns[0].stringValue());
        Assert.assertFalse(returns[0].stringValue().startsWith("w11"),
                "Returned wrong value:" + returns[0].stringValue());
    }

    @Test
    public void errorAfterSendTest() {

        BValue[] returns = BRunUtil.invoke(result, "errorResult");
        Assert.assertTrue(returns[0] instanceof BError);
        Assert.assertEquals(((BError) returns[0]).reason, "error3");
    }

    @Test
    public void panicAfterSendTest() {

        Exception expectedException = null;
        try {
            BRunUtil.invoke(result, "panicTest");
        } catch (Exception e) {
            expectedException = e;
        }
        Assert.assertNotNull(expectedException);
        String result = "error: error3 {\"message\":\"msg3\"}\n" + "\tat $lambda$14(sync-send.bal:236)";
        Assert.assertEquals(expectedException.getMessage().trim(), result.trim());
    }
}
