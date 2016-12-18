/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.lib.ehm.common;

import io.qio.qa.lib.common.BaseHelper;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.idm.apiHelpers.MUserGroupAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.asset.MAssetAPIHelper;
import io.qio.qa.lib.ehm.apiHelpers.tenant.MTenantAPIHelper;
import io.qio.qa.lib.ehm.model.asset.AssetRequest;
import io.qio.qa.lib.ehm.model.asset.AssetResponse;
import io.qio.qa.lib.ehm.model.asset.helper.AssetRequestHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeParameter;
import io.qio.qa.lib.ehm.model.assetType.helper.AttributeDataType;
import io.qio.qa.lib.ehm.model.assetType.helper.ParameterDataType;
import io.qio.qa.lib.common.MAbstractAPIHelper;
import io.qio.qa.lib.ehm.model.asset.AssetRequest;
import io.qio.qa.lib.ehm.common.TenantUtil;
import io.qio.qa.lib.mongoHelpers.MAbstractMongoHelper;

import java.util.ArrayList;

public class AssetUtil extends BaseTestUtil {

	private MAssetAPIHelper assetAPI = new MAssetAPIHelper();
	private static MAssetTypeAPIHelper assetTypeAPI;
	private static MTenantAPIHelper tenantAPI;
	private static MUserGroupAPIHelper groupIDMAPI;
	private static TenantUtil tenantUtil;

	private AssetRequestHelper assetRequestHelper;
	private static AssetRequest requestAsset;
	private final String MICROSERVICE_NAME = "asset";
	private String userType = "user";

	public AssetResponse createAssetWithCreatingAssetTypeAndTenant(String assetTypeFlavor, AttributeDataType attributeDataType, ParameterDataType parameterDataType) {
		initSetup(userType);
		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = assetRequestHelper.getAssetWithCreatingAssetTypeAndTenant(assetTypeFlavor, attributeDataType, parameterDataType);

		return MAbstractAPIHelper.getResponseObjForCreate(requestAsset, assetMicroservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
	}

	public AssetResponse createAssetWithPredefinedTenantAndWithCreatingAssetType(String assetTypeFlavor, AttributeDataType attributeDataType, ParameterDataType parameterDataType, String tenantId) {
		initSetup(userType);
		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = assetRequestHelper.getAssetWithPredefinedTenantAndWithCreatingAssetType(assetTypeFlavor, attributeDataType, parameterDataType, tenantId);

		return MAbstractAPIHelper.getResponseObjForCreate(requestAsset, assetMicroservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
	}

	public AssetResponse createAssetWithPredefinedAssetTypeAndWithCreatingTenant(String assetTypeId) {
		initSetup(userType);
		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		assetRequestHelper = new AssetRequestHelper();
		requestAsset = assetRequestHelper.getAssetWithPredefinedAssetTypeAndWithCreatingTenant(assetTypeId);

		return MAbstractAPIHelper.getResponseObjForCreate(requestAsset, assetMicroservice, environment, apiRequestHelper, assetAPI, AssetResponse.class);
	}

	public String getTenantForAsset (String assetId) {
		initSetup(userType);

		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(assetMicroservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		return(committedAsset.getTenant());
	}

	public String getAssetTypeForAsset (String assetId) {
		initSetup(userType);

		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(assetMicroservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		return(committedAsset.getAssetType().getAssetTypeId());
	}

	public AssetType getAssetTypeObjectForAsset (String assetId) {
		initSetup(userType);

		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(assetMicroservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);
		return(committedAsset.getAssetType());
	}

	public AssetTypeParameter getAssetTypeParameterObjectForAssetAndParameterId (String assetId, String parameterId) {
		initSetup(userType);

		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(assetMicroservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);

		for (AssetTypeParameter parameter : committedAsset.getAssetType().getParameters()) {
			if (parameter.getParameterId().equals(parameterId)) return parameter;
		}
		return null;
	}

	public AssetResponse getAssetObjectForAssetId (String assetId) {
		initSetup(userType);

		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);

		AssetResponse committedAsset = MAbstractAPIHelper.getResponseObjForRetrieve(assetMicroservice, environment, assetId, apiRequestHelper, assetAPI, AssetResponse.class);

		return committedAsset;
	}

	public void deleteAssetCollectionItemsAndDependencies ( ArrayList<String> idsForAllCreatedAssets) {
		groupIDMAPI = new MUserGroupAPIHelper();
		tenantAPI = new MTenantAPIHelper();
		assetAPI = new MAssetAPIHelper();
		assetTypeAPI = new MAssetTypeAPIHelper();
		tenantUtil = new TenantUtil();

		ArrayList<String> idsForAllCreatedTenants = new ArrayList<>();
		ArrayList<String> idsForAllCreatedAssetTypes = new ArrayList<>();
		ArrayList<String> idsForAllCreatedIDMGroupsForTenants = new ArrayList<>();

		for (String elementId : idsForAllCreatedAssets) {
			String tenantId = getTenantForAsset(elementId);
			String assetTypeId = getAssetTypeForAsset(elementId);
			idsForAllCreatedTenants.add(tenantId);
			idsForAllCreatedAssetTypes.add(assetTypeId);
		}

		for (String elementId : idsForAllCreatedTenants) {
			String groupId = tenantUtil.getIDMGroupForTenant(elementId);
			idsForAllCreatedIDMGroupsForTenants.add(groupId);
		}

		logger.info("Delete assets and asset types");
		String assetMicroservice = microserviceConfig.getString(MICROSERVICE_NAME + "." + envRuntime);
		BaseHelper.deleteListOfCollectionItems(assetMicroservice, environment, apiRequestHelper, assetAPI, idsForAllCreatedAssets);
		BaseHelper.deleteListOfCollectionItems(assetMicroservice, environment, apiRequestHelper, assetTypeAPI, idsForAllCreatedAssetTypes);

		logger.info("Delete tenants");
		String tenantMicroservice = microserviceConfig.getString("tenant" + "." + envRuntime);
		BaseHelper.deleteListOfCollectionItems(tenantMicroservice, environment, apiRequestHelper, tenantAPI, idsForAllCreatedTenants);

		//The API call above does not exist - hence doing a direct delete in Mongo
		baseInitMongoSetupBeforeAllTests("tenant");
		String URI = "mongodb://"+mongoUsername+":"+mongoPassword+"@"+mongoDbServer+":"+mongoDbServerPort+"/"+mongoDb;
		String mongoCollection = "tenant";
		MAbstractMongoHelper.deleteCollectionItemsFromMongoDbCollectionBasedOnElementValue(idsForAllCreatedTenants, URI, mongoDb, mongoCollection, "_id");

		logger.info("Delete idm groups");
		String oauthMicroservice = microserviceConfig.getString(oauthMicroserviceName + "." + envRuntime);
		BaseHelper.deleteListOfCollectionItems(oauthMicroservice, environment, apiRequestHelper, groupIDMAPI, idsForAllCreatedIDMGroupsForTenants);
	}
}