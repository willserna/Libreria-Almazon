package executer;

import AlmazonModel.AlmazonModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SetFlag extends ActionExecuterAbstractBase {

    public final static String NAME = "set-flag";
    public final static String PARAM_ACTIVE = "active";

    protected NodeService nodeService;

    private static Log logger = LogFactory.getLog(SetFlag.class);

    @Override
    protected void executeImpl(Action action, NodeRef actionedUponNodeRef){

        Boolean activeFlag = (Boolean)action.getParameterValue(PARAM_ACTIVE);

        if (activeFlag == null) activeFlag = true;

        if (logger.isDebugEnabled()) logger.debug("Inside executeImpl");

        // set the sc:isActive property to true
        // set the sc:published property to now
        Map<QName, Serializable> properties = nodeService.getProperties(actionedUponNodeRef);
        properties.put(QName.createQName(AlmazonModel.NAMESPACE_ALMAZON_CONTENT_MODEL, AlmazonModel.PROP_IS_ACTIVE), activeFlag);

        if (activeFlag) {
            properties.put(QName.createQName(AlmazonModel.NAMESPACE_ALMAZON_CONTENT_MODEL, AlmazonModel.PROP_PUBLISHED), new Date());
        }

        // if the aspect has already been added, set the properties
        if (nodeService.hasAspect(actionedUponNodeRef,
                QName.createQName(
                        AlmazonModel.NAMESPACE_ALMAZON_CONTENT_MODEL,
                        AlmazonModel.ASPECT_AL_WEBABLE))) {
            if (logger.isDebugEnabled()) logger.debug("Node has aspect");
            nodeService.setProperties(actionedUponNodeRef, properties);
        } else {
            // otherwise, add the aspect and set the properties
            if (logger.isDebugEnabled()) logger.debug("Node does not have aspect");
            nodeService.addAspect(actionedUponNodeRef, QName.createQName(AlmazonModel.NAMESPACE_ALMAZON_CONTENT_MODEL, AlmazonModel.ASPECT_AL_WEBABLE), properties);
        }

        if (logger.isDebugEnabled()) logger.debug("Ran web enable/disable action");
    }

    @Override
    protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
        paramList.add(new ParameterDefinitionImpl(PARAM_ACTIVE,
                DataTypeDefinition.BOOLEAN,
                false,
                getParamDisplayLabel(PARAM_ACTIVE)));
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
