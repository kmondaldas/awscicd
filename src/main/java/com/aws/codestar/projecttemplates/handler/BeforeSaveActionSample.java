package com.aws.codestar.projecttemplates.handler;

import java.util.List;

import com.reltio.lifecycle.framework.IAttributeValue;
import com.reltio.lifecycle.framework.IAttributes;
import com.reltio.lifecycle.framework.ILifeCycleObjectData;
import com.reltio.lifecycle.framework.IObject;
import com.reltio.lifecycle.framework.IReltioAPI;
import com.reltio.lifecycle.framework.ISimpleAttributeValue;
import com.reltio.lifecycle.lambda.LifeCycleActionHandler;

public class BeforeSaveActionSample extends LifeCycleActionHandler {

	@Override
	public ILifeCycleObjectData beforeSave(IReltioAPI reltioAPI, ILifeCycleObjectData data) {
		IObject object = data.getObject();
		IAttributes attributes = object.getAttributes();
		try {
			List<IAttributeValue> status = attributes.getAttributeValues("Status");
			ISimpleAttributeValue statusVal = (ISimpleAttributeValue) status.get(0);
			System.out.println("statusVal::::::::::::::" +statusVal);
			String statusNew = "Pending";
			ISimpleAttributeValue newValue = attributes.createSimpleAttributeValue("Status").value(statusNew).build();
			attributes.addAttributeValue(newValue);
			reltioAPI.logInfo("Status changed to Pending");
			return data;

		} catch (Exception ex) {
			reltioAPI.logError(ex.getMessage());
			throw new RuntimeException("BeforeSaveActionSample: LCA invocation failed.");
		}

	}


}  
