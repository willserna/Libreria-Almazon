package executer;

import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.rest.api.model.ActionDefinition;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.QNamePattern;

import java.util.List;

public class MoveReplacedActionExecuter extends ActionExecuterAbstractBase {

    public static final String NAME = "move-replaced";
    public static final String PARAM_DESTINATION_FOLDER = "destination-folder";

    private FileFolderService fileFolderService;
    private NodeService nodeService;

    @Override
    protected void addParameterDefinitions(List<ParameterDefinition> paramList){
        paramList.add(
                new ParameterDefinitionImpl(PARAM_DESTINATION_FOLDER,
                        DataTypeDefinition.NODE_REF,
                        true,
                        getParamDisplayLabel(PARAM_DESTINATION_FOLDER)));
    }

    @Override
    protected void executeImpl(Action ruleAction, NodeRef actionedUponNodeRef) {

        List<AssociationRef> assocRefs = nodeService.getTargetAssocs(actionedUponNodeRef,
                ((QNamePattern) QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "replaces")));

        if (assocRefs.isEmpty()) {
            return;
        } else {
            NodeRef destinationParent = (NodeRef) ruleAction.getParameterValue(PARAM_DESTINATION_FOLDER);
            for (AssociationRef assocNode : assocRefs) {

                NodeRef targetNodeRef = assocNode.getTargetRef();

                if (this.nodeService.exists(targetNodeRef) == true) {
                    try {
                        fileFolderService.move(targetNodeRef, destinationParent, null);
                    } catch (FileNotFoundException e) {

                    }
                }
            }

        }
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
