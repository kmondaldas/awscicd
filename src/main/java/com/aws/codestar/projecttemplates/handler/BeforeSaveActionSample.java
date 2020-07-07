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
		String userType = object.getUpdatedBy();

		try {

			if (null != userType && !userType.equalsIgnoreCase("PDM_Batch_User")) {

				if (null != attributes.getAttributeValues("ValidationStatus")

						&& !attributes.getAttributeValues("ValidationStatus").isEmpty()) {

					List<IAttributeValue> status = attributes.getAttributeValues("ValidationStatus");

					List<IAttributeValue> errorMsg = attributes.getAttributeValues("ErrorMessage");

					ISimpleAttributeValue statusVal = (ISimpleAttributeValue) status.get(0);

					ISimpleAttributeValue validationErrorMsg = (ISimpleAttributeValue) errorMsg.get(0);

					statusVal.setValue("Pending");

					validationErrorMsg.setValue("  ");

					reltioAPI.logInfo("BeforeSaveActionProvRecordValidationStatus:Existing status changed to Pending");

				} else {

					String statusNew = "Pending";

					String errMsgNew = "  ";

					ISimpleAttributeValue newValue = attributes.createSimpleAttributeValue("ValidationStatus")

							.value(statusNew).build();

					ISimpleAttributeValue newErrMsg = attributes.createSimpleAttributeValue("ErrorMessage")

							.value(errMsgNew).build();

					attributes.addAttributeValue(newValue);

					attributes.addAttributeValue(newErrMsg);

					object.getCrosswalks().getCrosswalks().get(0).getAttributes().addAttribute(newValue);

					object.getCrosswalks().getCrosswalks().get(0).getAttributes().addAttribute(newErrMsg);

					reltioAPI.logInfo("BeforeSaveActionProvRecordValidationStatus:Status changed to Pending");

				}

			}

			return data;

		} catch (Exception ex) {

			reltioAPI.logError(ex.getMessage());

			throw new RuntimeException("BeforeSaveActionProvRecordValidationStatus: LCA execution failed.", ex);

		}

	}

}
