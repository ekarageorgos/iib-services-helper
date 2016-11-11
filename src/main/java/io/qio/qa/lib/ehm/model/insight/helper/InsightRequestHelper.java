/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.lib.ehm.model.insight.helper;

import io.qio.qa.lib.ehm.common.InsightTypeUtil;
import io.qio.qa.lib.ehm.common.TenantUtil;
import io.qio.qa.lib.ehm.model.insight.InsightRequest;
import io.qio.qa.lib.ehm.model.insightType.InsightType;
import io.qio.qa.lib.ehm.model.insightType.helper.AttributeDataType;
import io.qio.qa.lib.ehm.model.tenant.Tenant;
import static io.qio.qa.lib.common.BaseHelper.getCurrentTimeStamp;

public class InsightRequestHelper {
	InsightRequest insight = null;
	InsightTypeUtil insightTypeUtil = null;
	TenantUtil tenantUtil = null;

	private void initDefaultInsight() {
		insight = new InsightRequest(getCurrentTimeStamp(), "", "");
	}

	public InsightRequest getInsightWithPredefinedInsightTypeAndTenant(String insightTypeId, String tenantId) {
		initDefaultInsight();
		insight.setInsightTypeId(insightTypeId);
		insight.setTenantId(tenantId);
		return insight;
	}

	public InsightRequest getInsightWithCreatingInsightTypeAndTenant(String insightTypeFlavor, AttributeDataType attributeDataType) {
		insightTypeUtil = new InsightTypeUtil();
		tenantUtil = new TenantUtil();

		InsightType insightType = insightTypeUtil.createInsightType(insightTypeFlavor, attributeDataType);
		String insightTypeId = insightType.getInsightTypeId();

		Tenant tenant = tenantUtil.createTenant();
		String tenantId = tenant.getTenantId();

		return getInsightWithPredefinedInsightTypeAndTenant(insightTypeId, tenantId);
	}
}