/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the FOAF vocabulary.
 */
package de.dfki.resc28.ole.bootstrap.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class FOAF 
{
  public static final String PREFIX = "FOAF";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource OnlineChatAccount = resource(CONSTANTS.CLASS_OnlineChatAccount);
  public static final Resource PersonalProfileDocument = resource(CONSTANTS.CLASS_PersonalProfileDocument);
  public static final Resource OnlineEcommerceAccount = resource(CONSTANTS.CLASS_OnlineEcommerceAccount);
  public static final Resource Image = resource(CONSTANTS.CLASS_Image);
  public static final Resource Organization = resource(CONSTANTS.CLASS_Organization);
  public static final Resource Document = resource(CONSTANTS.CLASS_Document);
  public static final Resource Group = resource(CONSTANTS.CLASS_Group);
  public static final Resource OnlineAccount = resource(CONSTANTS.CLASS_OnlineAccount);
  public static final Resource OnlineGamingAccount = resource(CONSTANTS.CLASS_OnlineGamingAccount);
  public static final Resource Person = resource(CONSTANTS.CLASS_Person);
  public static final Resource LabelProperty = resource(CONSTANTS.CLASS_LabelProperty);
  public static final Resource Agent = resource(CONSTANTS.CLASS_Agent);
  public static final Resource Project = resource(CONSTANTS.CLASS_Project);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property mbox = property(CONSTANTS.PROP_mbox);
  public static final Property isPrimaryTopicOf = property(CONSTANTS.PROP_isPrimaryTopicOf);
  public static final Property surname = property(CONSTANTS.PROP_surname);
  public static final Property yahooChatID = property(CONSTANTS.PROP_yahooChatID);
  public static final Property msnChatID = property(CONSTANTS.PROP_msnChatID);
  public static final Property skypeID = property(CONSTANTS.PROP_skypeID);
  public static final Property topic_interest = property(CONSTANTS.PROP_topic_interest);
  public static final Property title = property(CONSTANTS.PROP_title);
  public static final Property icqChatID = property(CONSTANTS.PROP_icqChatID);
  public static final Property dnaChecksum = property(CONSTANTS.PROP_dnaChecksum);
  public static final Property based_near = property(CONSTANTS.PROP_based_near);
  public static final Property phone = property(CONSTANTS.PROP_phone);
  public static final Property publications = property(CONSTANTS.PROP_publications);
  public static final Property gender = property(CONSTANTS.PROP_gender);
  public static final Property givenname = property(CONSTANTS.PROP_givenname);
  public static final Property myersBriggs = property(CONSTANTS.PROP_myersBriggs);
  public static final Property account = property(CONSTANTS.PROP_account);
  public static final Property weblog = property(CONSTANTS.PROP_weblog);
  public static final Property theme = property(CONSTANTS.PROP_theme);
  public static final Property depiction = property(CONSTANTS.PROP_depiction);
  public static final Property workplaceHomepage = property(CONSTANTS.PROP_workplaceHomepage);
  public static final Property maker = property(CONSTANTS.PROP_maker);
  public static final Property pastProject = property(CONSTANTS.PROP_pastProject);
  public static final Property jabberID = property(CONSTANTS.PROP_jabberID);
  public static final Property age = property(CONSTANTS.PROP_age);
  public static final Property lastName = property(CONSTANTS.PROP_lastName);
  public static final Property logo = property(CONSTANTS.PROP_logo);
  public static final Property givenName = property(CONSTANTS.PROP_givenName);
  public static final Property openid = property(CONSTANTS.PROP_openid);
  public static final Property topic = property(CONSTANTS.PROP_topic);
  public static final Property mbox_sha1sum = property(CONSTANTS.PROP_mbox_sha1sum);
  public static final Property birthday = property(CONSTANTS.PROP_birthday);
  public static final Property primaryTopic = property(CONSTANTS.PROP_primaryTopic);
  public static final Property name = property(CONSTANTS.PROP_name);
  public static final Property knows = property(CONSTANTS.PROP_knows);
  public static final Property plan = property(CONSTANTS.PROP_plan);
  public static final Property homepage = property(CONSTANTS.PROP_homepage);
  public static final Property membershipClass = property(CONSTANTS.PROP_membershipClass);
  public static final Property tipjar = property(CONSTANTS.PROP_tipjar);
  public static final Property currentProject = property(CONSTANTS.PROP_currentProject);
  public static final Property accountServiceHomepage = property(CONSTANTS.PROP_accountServiceHomepage);
  public static final Property member = property(CONSTANTS.PROP_member);
  public static final Property page = property(CONSTANTS.PROP_page);
  public static final Property sha1 = property(CONSTANTS.PROP_sha1);
  public static final Property interest = property(CONSTANTS.PROP_interest);
  public static final Property schoolHomepage = property(CONSTANTS.PROP_schoolHomepage);
  public static final Property fundedBy = property(CONSTANTS.PROP_fundedBy);
  public static final Property familyName = property(CONSTANTS.PROP_familyName);
  public static final Property status = property(CONSTANTS.PROP_status);
  public static final Property holdsAccount = property(CONSTANTS.PROP_holdsAccount);
  public static final Property accountName = property(CONSTANTS.PROP_accountName);
  public static final Property thumbnail = property(CONSTANTS.PROP_thumbnail);
  public static final Property geekcode = property(CONSTANTS.PROP_geekcode);
  public static final Property made = property(CONSTANTS.PROP_made);
  public static final Property aimChatID = property(CONSTANTS.PROP_aimChatID);
  public static final Property workInfoHomepage = property(CONSTANTS.PROP_workInfoHomepage);
  public static final Property nick = property(CONSTANTS.PROP_nick);
  public static final Property depicts = property(CONSTANTS.PROP_depicts);
  public static final Property firstName = property(CONSTANTS.PROP_firstName);
  public static final Property family_name = property(CONSTANTS.PROP_family_name);
  public static final Property focus = property(CONSTANTS.PROP_focus);
  public static final Property img = property(CONSTANTS.PROP_img);


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
    private static final String NS = "http://xmlns.com/foaf/0.1/#";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_OnlineChatAccount = "OnlineChatAccount";
    private static final String CLASS_OnlineChatAccount = nsName(CLASS_LNAME_OnlineChatAccount);
    private static final String CLASS_LNAME_PersonalProfileDocument = "PersonalProfileDocument";
    private static final String CLASS_PersonalProfileDocument = nsName(CLASS_LNAME_PersonalProfileDocument);
    private static final String CLASS_LNAME_OnlineEcommerceAccount = "OnlineEcommerceAccount";
    private static final String CLASS_OnlineEcommerceAccount = nsName(CLASS_LNAME_OnlineEcommerceAccount);
    private static final String CLASS_LNAME_Image = "Image";
    private static final String CLASS_Image = nsName(CLASS_LNAME_Image);
    private static final String CLASS_LNAME_Organization = "Organization";
    private static final String CLASS_Organization = nsName(CLASS_LNAME_Organization);
    private static final String CLASS_LNAME_Document = "Document";
    private static final String CLASS_Document = nsName(CLASS_LNAME_Document);
    private static final String CLASS_LNAME_Group = "Group";
    private static final String CLASS_Group = nsName(CLASS_LNAME_Group);
    private static final String CLASS_LNAME_OnlineAccount = "OnlineAccount";
    private static final String CLASS_OnlineAccount = nsName(CLASS_LNAME_OnlineAccount);
    private static final String CLASS_LNAME_OnlineGamingAccount = "OnlineGamingAccount";
    private static final String CLASS_OnlineGamingAccount = nsName(CLASS_LNAME_OnlineGamingAccount);
    private static final String CLASS_LNAME_Person = "Person";
    private static final String CLASS_Person = nsName(CLASS_LNAME_Person);
    private static final String CLASS_LNAME_LabelProperty = "LabelProperty";
    private static final String CLASS_LabelProperty = nsName(CLASS_LNAME_LabelProperty);
    private static final String CLASS_LNAME_Agent = "Agent";
    private static final String CLASS_Agent = nsName(CLASS_LNAME_Agent);
    private static final String CLASS_LNAME_Project = "Project";
    private static final String CLASS_Project = nsName(CLASS_LNAME_Project);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_mbox = "mbox";
    private static final String PROP_mbox = nsName(PROP_LNAME_mbox);
    private static final String PROP_LNAME_isPrimaryTopicOf = "isPrimaryTopicOf";
    private static final String PROP_isPrimaryTopicOf = nsName(PROP_LNAME_isPrimaryTopicOf);
    private static final String PROP_LNAME_surname = "surname";
    private static final String PROP_surname = nsName(PROP_LNAME_surname);
    private static final String PROP_LNAME_yahooChatID = "yahooChatID";
    private static final String PROP_yahooChatID = nsName(PROP_LNAME_yahooChatID);
    private static final String PROP_LNAME_msnChatID = "msnChatID";
    private static final String PROP_msnChatID = nsName(PROP_LNAME_msnChatID);
    private static final String PROP_LNAME_skypeID = "skypeID";
    private static final String PROP_skypeID = nsName(PROP_LNAME_skypeID);
    private static final String PROP_LNAME_topic_interest = "topic_interest";
    private static final String PROP_topic_interest = nsName(PROP_LNAME_topic_interest);
    private static final String PROP_LNAME_title = "title";
    private static final String PROP_title = nsName(PROP_LNAME_title);
    private static final String PROP_LNAME_icqChatID = "icqChatID";
    private static final String PROP_icqChatID = nsName(PROP_LNAME_icqChatID);
    private static final String PROP_LNAME_dnaChecksum = "dnaChecksum";
    private static final String PROP_dnaChecksum = nsName(PROP_LNAME_dnaChecksum);
    private static final String PROP_LNAME_based_near = "based_near";
    private static final String PROP_based_near = nsName(PROP_LNAME_based_near);
    private static final String PROP_LNAME_phone = "phone";
    private static final String PROP_phone = nsName(PROP_LNAME_phone);
    private static final String PROP_LNAME_publications = "publications";
    private static final String PROP_publications = nsName(PROP_LNAME_publications);
    private static final String PROP_LNAME_gender = "gender";
    private static final String PROP_gender = nsName(PROP_LNAME_gender);
    private static final String PROP_LNAME_givenname = "givenname";
    private static final String PROP_givenname = nsName(PROP_LNAME_givenname);
    private static final String PROP_LNAME_myersBriggs = "myersBriggs";
    private static final String PROP_myersBriggs = nsName(PROP_LNAME_myersBriggs);
    private static final String PROP_LNAME_account = "account";
    private static final String PROP_account = nsName(PROP_LNAME_account);
    private static final String PROP_LNAME_weblog = "weblog";
    private static final String PROP_weblog = nsName(PROP_LNAME_weblog);
    private static final String PROP_LNAME_theme = "theme";
    private static final String PROP_theme = nsName(PROP_LNAME_theme);
    private static final String PROP_LNAME_depiction = "depiction";
    private static final String PROP_depiction = nsName(PROP_LNAME_depiction);
    private static final String PROP_LNAME_workplaceHomepage = "workplaceHomepage";
    private static final String PROP_workplaceHomepage = nsName(PROP_LNAME_workplaceHomepage);
    private static final String PROP_LNAME_maker = "maker";
    private static final String PROP_maker = nsName(PROP_LNAME_maker);
    private static final String PROP_LNAME_pastProject = "pastProject";
    private static final String PROP_pastProject = nsName(PROP_LNAME_pastProject);
    private static final String PROP_LNAME_jabberID = "jabberID";
    private static final String PROP_jabberID = nsName(PROP_LNAME_jabberID);
    private static final String PROP_LNAME_age = "age";
    private static final String PROP_age = nsName(PROP_LNAME_age);
    private static final String PROP_LNAME_lastName = "lastName";
    private static final String PROP_lastName = nsName(PROP_LNAME_lastName);
    private static final String PROP_LNAME_logo = "logo";
    private static final String PROP_logo = nsName(PROP_LNAME_logo);
    private static final String PROP_LNAME_givenName = "givenName";
    private static final String PROP_givenName = nsName(PROP_LNAME_givenName);
    private static final String PROP_LNAME_openid = "openid";
    private static final String PROP_openid = nsName(PROP_LNAME_openid);
    private static final String PROP_LNAME_topic = "topic";
    private static final String PROP_topic = nsName(PROP_LNAME_topic);
    private static final String PROP_LNAME_mbox_sha1sum = "mbox_sha1sum";
    private static final String PROP_mbox_sha1sum = nsName(PROP_LNAME_mbox_sha1sum);
    private static final String PROP_LNAME_birthday = "birthday";
    private static final String PROP_birthday = nsName(PROP_LNAME_birthday);
    private static final String PROP_LNAME_primaryTopic = "primaryTopic";
    private static final String PROP_primaryTopic = nsName(PROP_LNAME_primaryTopic);
    private static final String PROP_LNAME_name = "name";
    private static final String PROP_name = nsName(PROP_LNAME_name);
    private static final String PROP_LNAME_knows = "knows";
    private static final String PROP_knows = nsName(PROP_LNAME_knows);
    private static final String PROP_LNAME_plan = "plan";
    private static final String PROP_plan = nsName(PROP_LNAME_plan);
    private static final String PROP_LNAME_homepage = "homepage";
    private static final String PROP_homepage = nsName(PROP_LNAME_homepage);
    private static final String PROP_LNAME_membershipClass = "membershipClass";
    private static final String PROP_membershipClass = nsName(PROP_LNAME_membershipClass);
    private static final String PROP_LNAME_tipjar = "tipjar";
    private static final String PROP_tipjar = nsName(PROP_LNAME_tipjar);
    private static final String PROP_LNAME_currentProject = "currentProject";
    private static final String PROP_currentProject = nsName(PROP_LNAME_currentProject);
    private static final String PROP_LNAME_accountServiceHomepage = "accountServiceHomepage";
    private static final String PROP_accountServiceHomepage = nsName(PROP_LNAME_accountServiceHomepage);
    private static final String PROP_LNAME_member = "member";
    private static final String PROP_member = nsName(PROP_LNAME_member);
    private static final String PROP_LNAME_page = "page";
    private static final String PROP_page = nsName(PROP_LNAME_page);
    private static final String PROP_LNAME_sha1 = "sha1";
    private static final String PROP_sha1 = nsName(PROP_LNAME_sha1);
    private static final String PROP_LNAME_interest = "interest";
    private static final String PROP_interest = nsName(PROP_LNAME_interest);
    private static final String PROP_LNAME_schoolHomepage = "schoolHomepage";
    private static final String PROP_schoolHomepage = nsName(PROP_LNAME_schoolHomepage);
    private static final String PROP_LNAME_fundedBy = "fundedBy";
    private static final String PROP_fundedBy = nsName(PROP_LNAME_fundedBy);
    private static final String PROP_LNAME_familyName = "familyName";
    private static final String PROP_familyName = nsName(PROP_LNAME_familyName);
    private static final String PROP_LNAME_status = "status";
    private static final String PROP_status = nsName(PROP_LNAME_status);
    private static final String PROP_LNAME_holdsAccount = "holdsAccount";
    private static final String PROP_holdsAccount = nsName(PROP_LNAME_holdsAccount);
    private static final String PROP_LNAME_accountName = "accountName";
    private static final String PROP_accountName = nsName(PROP_LNAME_accountName);
    private static final String PROP_LNAME_thumbnail = "thumbnail";
    private static final String PROP_thumbnail = nsName(PROP_LNAME_thumbnail);
    private static final String PROP_LNAME_geekcode = "geekcode";
    private static final String PROP_geekcode = nsName(PROP_LNAME_geekcode);
    private static final String PROP_LNAME_made = "made";
    private static final String PROP_made = nsName(PROP_LNAME_made);
    private static final String PROP_LNAME_aimChatID = "aimChatID";
    private static final String PROP_aimChatID = nsName(PROP_LNAME_aimChatID);
    private static final String PROP_LNAME_workInfoHomepage = "workInfoHomepage";
    private static final String PROP_workInfoHomepage = nsName(PROP_LNAME_workInfoHomepage);
    private static final String PROP_LNAME_nick = "nick";
    private static final String PROP_nick = nsName(PROP_LNAME_nick);
    private static final String PROP_LNAME_depicts = "depicts";
    private static final String PROP_depicts = nsName(PROP_LNAME_depicts);
    private static final String PROP_LNAME_firstName = "firstName";
    private static final String PROP_firstName = nsName(PROP_LNAME_firstName);
    private static final String PROP_LNAME_family_name = "family_name";
    private static final String PROP_family_name = nsName(PROP_LNAME_family_name);
    private static final String PROP_LNAME_focus = "focus";
    private static final String PROP_focus = nsName(PROP_LNAME_focus);
    private static final String PROP_LNAME_img = "img";
    private static final String PROP_img = nsName(PROP_LNAME_img);

 
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