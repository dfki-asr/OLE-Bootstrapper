/*
 * This file is part of OLE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */

package de.dfki.resc28.ole.bootstrap.listener;


import org.antlr.v4.runtime.RuleContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.LDrawParser.LDrawParser.Author_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParser.FileContext;
import de.dfki.resc28.LDrawParser.LDrawParser.History_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParserBaseListener;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.ole.bootstrap.App;
import de.dfki.resc28.ole.bootstrap.Util;
import de.dfki.resc28.ole.bootstrap.vocabularies.ADMS;
import de.dfki.resc28.ole.bootstrap.vocabularies.DCAT;
import de.dfki.resc28.ole.bootstrap.vocabularies.FOAF;

// FIXME: this is broken!
public class UserListener extends LDrawParserBaseListener
{
	private Model userModel;
	private Resource asset;
	private Resource user;

	private String fileName;
	private String basename;

	private IGraphStore graphStore;

	public UserListener(String fileName, IGraphStore graphStore)
	{
		super();
		
		this.fileName  = fileName;
		this.basename = FilenameUtils.getBaseName(fileName);
		this.graphStore = graphStore;
	}
	
	@Override
	public void enterFile(FileContext ctx) 
	{
		userModel = ModelFactory.createDefaultModel();
		userModel.setNsPrefixes(FOAF.NAMESPACE);
		userModel.setNsPrefixes(ADMS.NAMESPACE);
		userModel.setNsPrefixes(DCAT.NAMESPACE);
		userModel.setNsPrefix("dcterms", DCTerms.NS);
		userModel.setNsPrefix("rdf", RDF.getURI());
		userModel.setNsPrefix("rdfs", RDFS.getURI());
		userModel.setNsPrefix("skos", SKOS.getURI());
		userModel.setNsPrefix("xsd", XSD.NS);
		userModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		userModel.setNsPrefix("users", App.fUserBaseUri);
		userModel.setNsPrefix("assets", App.fAssetBaseUri);
		userModel.setNsPrefix("distributions", App.fDistributionBaseUri);

		asset = userModel.createResource(Util.joinPath(App.fAssetBaseUri, Util.urlEncoded(basename)));

	};

	@Override
	public void exitFile(FileContext ctx)
	{
		if (user != null)
		{
			graphStore.addToNamedGraph(user.getURI(), userModel);
			
			// slow down to prevent org.apache.jena.atlas.web.HttpException: 500 - Direct buffer memory
			try 
			{
				Thread.sleep(50);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	
	@Override
	public void exitAuthor_row(Author_rowContext ctx)
	{
		if (ctx != null)
		{
			if (ctx.realname() != null)
			{
				Resource creator = ResourceFactory.createResource(Util.joinPath(App.fUserBaseUri, Util.toURLEncodedStringLiteral(ctx.realname().STRING(), "_").toString()));
				
				
				userModel.add( user, FOAF.made, asset);
				
				// only add user if not known yet!
				if (!graphStore.containsNamedGraph(user.getURI()))
				{
					userModel.add( user, RDF.type, FOAF.Agent );
					userModel.add( user, FOAF.name, Util.toStringLiteral(ctx.realname().STRING(), " ") );

					if (ctx.username() != null)
					{
						Resource account = userModel.createResource();
						userModel.add( account, RDF.type, FOAF.OnlineAccount );
						userModel.add( account, FOAF.accountServiceHomepage, userModel.createTypedLiteral("http://ldraw.org/", XSDDatatype.XSDanyURI) );
						userModel.add( account, FOAF.accountName, ctx.username().STRING().getText() );
						userModel.add( user, FOAF.account, account );
					}				
				}
			}
		}
	}

	
//	@Override
//	public void exitHistory_row(History_rowContext ctx)
//	{
//		if (ctx != null)
//		{
//			if (ctx.realname() != null)
//			{
//				Resource contributor = ResourceFactory.createResource(Util.joinPath(App.fUserBaseUri, Util.toURLEncodedStringLiteral(ctx.realname().STRING(), "_").toString()));
//				userModel.add( contributor, DCTerms.contributor, asset );
//				
//				// only add user if not known yet!
//				if (!graphStore.containsNamedGraph(contributor.getURI()))
//				{
//					userModel.add( user, RDF.type, FOAF.Agent );
//					userModel.add( user, FOAF.name, Util.toStringLiteral(ctx.realname().STRING(), " ") );
//					
//					userModel.add( creator, RDF.type, FOAF.Agent );
//					userModel.add( creator, FOAF.name, Util.toStringLiteral(ctx.realname().STRING(), " ") );
//				}
//			}
//
//			if (ctx.username() != null)
//			{
//				// TODO: // check if we know a user with ctx.username() else create this user
//			}
//		}
//	}
}
