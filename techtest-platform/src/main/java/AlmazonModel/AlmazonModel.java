package AlmazonModel;

public interface AlmazonModel {

    // Types
    public static final String NAMESPACE_ALMAZON_CONTENT_MODEL  = "http://www.almazon.com/model/content/1.0";
    public static final String TYPE_AL_DOC = "doc";


    // Aspects
    public static final String ASPECT_AL_WEBABLE = "webable";
    public static final String ASPECT_AL_PRODUCT_RELATED = "productRelated";

    // Properties
    public static final String PROP_PRODUCT = "product";
    public static final String PROP_VERSION = "version";
    public static final String PROP_PUBLISHED = "published";
    public static final String PROP_IS_ACTIVE = "isActive";

    // Associations
    public static final String ASSN_RELATED_DOCUMENTS = "relatedDocuments";
}
