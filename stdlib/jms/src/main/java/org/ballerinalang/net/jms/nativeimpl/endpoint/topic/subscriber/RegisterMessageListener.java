/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ballerinalang.net.jms.nativeimpl.endpoint.topic.subscriber;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.net.jms.AbstractBlockingAction;
import org.ballerinalang.net.jms.JmsConstants;
import org.ballerinalang.net.jms.nativeimpl.endpoint.common.MessageListenerHandler;

/**
 * Register JMS listener for a consumer endpoint.
 *
 * @since 0.970
 */

@BallerinaFunction(
        orgName = JmsConstants.BALLERINA, packageName = JmsConstants.JMS,
        functionName = "registerListener",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = JmsConstants.TOPIC_SUBSCRIBER_OBJ_NAME,
                             structPackage = JmsConstants.PROTOCOL_PACKAGE_JMS),
        args = {
                @Argument(name = "serviceType", type = TypeKind.TYPEDESC),
                @Argument(name = JmsConstants.METHOD_FIELD_ACTIONS, type = TypeKind.OBJECT,
                          structType = JmsConstants.DURABLE_TOPIC_SUBSCRIBER_CALLER_OBJ_NAME),
                @Argument(name = JmsConstants.METHOD_FIELD_DATA, type = TypeKind.MAP)
        },
        isPublic = true
)
public class RegisterMessageListener extends AbstractBlockingAction {

    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {
        MessageListenerHandler.createAndRegister(context);
    }

}
