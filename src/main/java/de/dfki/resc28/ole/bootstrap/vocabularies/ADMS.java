/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the ADMS vocabulary.
 */
package de.dfki.resc28.ole.bootstrap.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class ADMS 
{
  public static final String PREFIX = "ADMS";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource AssetRepository = resource(CONSTANTS.CLASS_AssetRepository);
  public static final Resource Identifier = resource(CONSTANTS.CLASS_Identifier);
  public static final Resource Asset = resource(CONSTANTS.CLASS_Asset);
  public static final Resource AssetDistribution = resource(CONSTANTS.CLASS_AssetDistribution);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property identifier = property(CONSTANTS.PROP_identifier);
  public static final Property schemeAgency = property(CONSTANTS.PROP_schemeAgency);
  public static final Property translation = property(CONSTANTS.PROP_translation);
  public static final Property sample = property(CONSTANTS.PROP_sample);
  public static final Property supportedSchema = property(CONSTANTS.PROP_supportedSchema);
  public static final Property last = property(CONSTANTS.PROP_last);
  public static final Property prev = property(CONSTANTS.PROP_prev);
  public static final Property interoperabilityLevel = property(CONSTANTS.PROP_interoperabilityLevel);
  public static final Property representationTechnique = property(CONSTANTS.PROP_representationTechnique);
  public static final Property next = property(CONSTANTS.PROP_next);
  public static final Property status = property(CONSTANTS.PROP_status);
  public static final Property versionNotes = property(CONSTANTS.PROP_versionNotes);
  public static final Property includedAsset = property(CONSTANTS.PROP_includedAsset);


  /**
   * Returns a Jena resource for the given namespace name 
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Resource
   */
  private static final Resource resource(String nsName)
  {
    return ResourceFactory.createResource(nsName); 
  }

  /**
   * Returns a Jena property for the given namespace name
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Property
   */
  private static final Property property(String nsName)
  { 
    return ResourceFactory.createProperty(nsName);
  }

  private static final class CONSTANTS 
  {
    /**
     * Vocabulary namespace URI as string 
     */
    private static final String NS = "http://www.w3.org/ns/adms#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_AssetRepository = "AssetRepository";
    private static final String CLASS_AssetRepository = nsName(CLASS_LNAME_AssetRepository);
    private static final String CLASS_LNAME_Identifier = "Identifier";
    private static final String CLASS_Identifier = nsName(CLASS_LNAME_Identifier);
    private static final String CLASS_LNAME_Asset = "Asset";
    private static final String CLASS_Asset = nsName(CLASS_LNAME_Asset);
    private static final String CLASS_LNAME_AssetDistribution = "AssetDistribution";
    private static final String CLASS_AssetDistribution = nsName(CLASS_LNAME_AssetDistribution);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_identifier = "identifier";
    private static final String PROP_identifier = nsName(PROP_LNAME_identifier);
    private static final String PROP_LNAME_schemeAgency = "schemeAgency";
    private static final String PROP_schemeAgency = nsName(PROP_LNAME_schemeAgency);
    private static final String PROP_LNAME_translation = "translation";
    private static final String PROP_translation = nsName(PROP_LNAME_translation);
    private static final String PROP_LNAME_sample = "sample";
    private static final String PROP_sample = nsName(PROP_LNAME_sample);
    private static final String PROP_LNAME_supportedSchema = "supportedSchema";
    private static final String PROP_supportedSchema = nsName(PROP_LNAME_supportedSchema);
    private static final String PROP_LNAME_last = "last";
    private static final String PROP_last = nsName(PROP_LNAME_last);
    private static final String PROP_LNAME_prev = "prev";
    private static final String PROP_prev = nsName(PROP_LNAME_prev);
    private static final String PROP_LNAME_interoperabilityLevel = "interoperabilityLevel";
    private static final String PROP_interoperabilityLevel = nsName(PROP_LNAME_interoperabilityLevel);
    private static final String PROP_LNAME_representationTechnique = "representationTechnique";
    private static final String PROP_representationTechnique = nsName(PROP_LNAME_representationTechnique);
    private static final String PROP_LNAME_next = "next";
    private static final String PROP_next = nsName(PROP_LNAME_next);
    private static final String PROP_LNAME_status = "status";
    private static final String PROP_status = nsName(PROP_LNAME_status);
    private static final String PROP_LNAME_versionNotes = "versionNotes";
    private static final String PROP_versionNotes = nsName(PROP_LNAME_versionNotes);
    private static final String PROP_LNAME_includedAsset = "includedAsset";
    private static final String PROP_includedAsset = nsName(PROP_LNAME_includedAsset);

 
    /**
     * Returns the full namespace name of a vocabulary element as a string
     * @param localName  the local name of a vocabulary element as a string
     * @return the full namespace name of a vocabulary element as a string
     */
    private static String nsName(String localName) 
    {
      return NS + localName;
    }
  }
}